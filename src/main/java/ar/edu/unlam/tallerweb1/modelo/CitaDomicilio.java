package ar.edu.unlam.tallerweb1.modelo;

import ar.edu.unlam.tallerweb1.modelo.datos.DatosCitaDomicilio;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "citaDomicilio_id")
public class CitaDomicilio extends Cita{

    private Float latitud;

    private Float longitud;

    private String sintomas;

    public Float getLatitud() {
        return latitud;
    }

    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public DatosCitaDomicilio toDatosCitaDomicilio(){
        DatosCitaDomicilio datos = new DatosCitaDomicilio();

        datos.setLatitud(this.latitud);
        datos.setLongitud(this.longitud);
        datos.setSintomas(this.sintomas);
        datos.setMedico(this.getMedico());
        datos.setEmailPaciente(this.getPaciente().getEmail());
        datos.setId(this.getId());

        return datos;
    }



}
