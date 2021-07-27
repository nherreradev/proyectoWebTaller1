package ar.edu.unlam.tallerweb1.controladores;

import ar.edu.unlam.tallerweb1.modelo.CitaConsultorio;
import ar.edu.unlam.tallerweb1.modelo.CitaDomicilio;
import ar.edu.unlam.tallerweb1.modelo.datos.DatosCitaConsultio;
import ar.edu.unlam.tallerweb1.modelo.datos.DatosCitaDomicilio;
import ar.edu.unlam.tallerweb1.servicios.ServicioCita;
import ar.edu.unlam.tallerweb1.servicios.ServicioCitaDomicilio;
import ar.edu.unlam.tallerweb1.servicios.ServicioCitaConsultorio;
import ar.edu.unlam.tallerweb1.servicios.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class ControladorPaciente {

    private ServicioCita servicioCita;
    private ServicioCitaDomicilio servicioCitaDomicilio;
    private ServicioPaciente servicioPaciente;
    private ServicioCitaConsultorio servicioCitaConsultorio;

    @Autowired
    public  ControladorPaciente(ServicioCita servicioCita, ServicioPaciente servicioPaciente, ServicioCitaConsultorio servicioCitaConsultorio, ServicioCitaDomicilio servicioCitaDomicilio){
        this.servicioCita = servicioCita;
        this.servicioPaciente = servicioPaciente;
        this.servicioCitaConsultorio = servicioCitaConsultorio;
        this.servicioCitaDomicilio = servicioCitaDomicilio;
    }

    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHomePaciente() {
        return new ModelAndView("redirect:/paciente/citas/index");
    }

    @RequestMapping("/citas/index")
    public ModelAndView irAMisCitas(Authentication authentication) {
        ModelMap model = new ModelMap();
        User user = (User) authentication.getPrincipal();
        model.put("citas", servicioPaciente.getCitaConsultorio(user.getUsername()));

        return new ModelAndView("mis-citas/index", model);
    }

    @RequestMapping("/citas/create")
    public ModelAndView irACrearCita(){
        ModelMap model = new ModelMap();
        model.put("datos", new DatosCitaConsultio());
        model.put("especialidades", servicioCita.allEspecialidades());
        model.put("fechaActual", LocalDate.now());

        return new ModelAndView("mis-citas/create", model);
    }

    @RequestMapping(value = "/citas/store", method = RequestMethod.POST)
    public ModelAndView createCita(@Valid DatosCitaConsultio datosCita, BindingResult result, Authentication authentication){
        ModelMap model = new ModelMap();
        List<String> errores = new ArrayList<>();
        User user = (User) authentication.getPrincipal();
        datosCita.setPaciente(user.getUsername());

        if (result.hasErrors()){
            result.getFieldErrors().forEach(error -> {
                errores.add(error.getDefaultMessage());
            });

            model.put("errores", errores);
            return new ModelAndView("redirect:/paciente/citas/create", model);
        }

        try {
            servicioCita.registrarCitaConsultorio(datosCita);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return new ModelAndView("mis-citas/create", model);
        }

        return new ModelAndView("redirect:/paciente/citas/index");
    }

    @RequestMapping("/citas/createDomicilio")
    public ModelAndView irACrearCitaDomicilio(){
        ModelMap model = new ModelMap();
        model.put("datos", new DatosCitaDomicilio());

        return new ModelAndView("mis-citas/createCitaDomicilio", model);
    }

    @RequestMapping(value = "/citas/storeCitaDomicilio", method = RequestMethod.POST)
    public ModelAndView createCitaDomicilio(@Valid DatosCitaDomicilio datosCita, BindingResult result, Authentication authentication){
        ModelMap model = new ModelMap();
        List<String> errores = new ArrayList<>();
        User user = (User) authentication.getPrincipal();
        datosCita.setEmailPaciente(user.getUsername());

        if (result.hasErrors()){
            result.getFieldErrors().forEach(error -> {
                errores.add(error.getDefaultMessage());
            });
            model.put("errores", errores);
            model.put("datos", datosCita);
            return new ModelAndView("redirect:/paciente/citas/createDomicilio", model);
        }

        try {
            servicioCitaDomicilio.createCitaDomicilio(datosCita);
        } catch (Exception e) {
            errores.add(e.getMessage());
            model.addAttribute("errores", errores);
            model.put("datos", datosCita);
            return new ModelAndView("mis-citas/createCitaDomicilio", model);
        }

        return new ModelAndView("redirect:/paciente/citas/medicoDomicilio");
    }

    @RequestMapping("/citas/medicoDomicilio")
    public ModelAndView irAMisCitasDomicilio(Authentication authentication) {
        ModelMap model = new ModelMap();
        User user = (User) authentication.getPrincipal();
        model.put("citas", servicioPaciente.getCitasDomicilioPend(user.getUsername()));

        return new ModelAndView("mis-citas/mis-citas-domicilio", model);
    }

    @RequestMapping(value = "/citas/cancelar/{id}", method = RequestMethod.GET)
    public ModelAndView cancelarCita(@PathVariable Long id, Authentication authentication){
        List<String> errores = new ArrayList<>();
        ModelMap model = new ModelMap();
        User user = (User) authentication.getPrincipal();
        try {
            servicioCitaDomicilio.cancelarCitaDomicilio(user.getUsername(), id);
        } catch (Exception e) {
            errores.add(e.getMessage());
            model.addAttribute("errores", errores);
            return new ModelAndView("mis-citas/mis-citas-domicilio", model);
        }

        return new ModelAndView("redirect:/paciente/citas/medicoDomicilio");
    }

    @RequestMapping(value = "/mapa/{id}", method = RequestMethod.GET)
    public ModelAndView mapaMedico(@PathVariable Long id){

        CitaConsultorio citaConsultorio = servicioCitaConsultorio.getCitaById(id);

        Float latitud = citaConsultorio.getMedico().getConsultorio().getLatitud();
        Float longitud = citaConsultorio.getMedico().getConsultorio().getLongitud();

        ModelMap model = new ModelMap();
        model.put("latitud", latitud);
        model.put("longitud", longitud);

        return new ModelAndView("maps/mapaMedico", model);
    }

}
