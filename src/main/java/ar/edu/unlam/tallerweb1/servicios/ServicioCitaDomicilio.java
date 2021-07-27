package ar.edu.unlam.tallerweb1.servicios;

import ar.edu.unlam.tallerweb1.excepciones.CancelarCitaDomicilioError;
import ar.edu.unlam.tallerweb1.excepciones.CrearCitaError;
import ar.edu.unlam.tallerweb1.modelo.CitaDomicilio;
import ar.edu.unlam.tallerweb1.modelo.Medico;
import ar.edu.unlam.tallerweb1.modelo.datos.DatosCitaDomicilio;

public interface ServicioCitaDomicilio {

    CitaDomicilio getCitaById(Long id);

    void createCitaDomicilio(DatosCitaDomicilio datosCita) throws CrearCitaError;

    void cancelarCitaDomicilio(String mailPaciente, Long idCitaDom) throws CancelarCitaDomicilioError;

    DatosCitaDomicilio obtenerMenosOcupado(Float lat_paciente, Float lon_paciente);

    Medico obtenerMedicoProximo(Float latitud, Float longitud);

    Long obtenerDemora(Long idCita);

}
