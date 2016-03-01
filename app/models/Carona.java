package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Carona extends Model {

    @Id
    private Long id;

    @ManyToOne
    private Aluno criador;

    @Constraints.Required
    private int vagas;

    @Constraints.Required
    private int tolerancia;

    @Constraints.Required
    private String data;

    @Constraints.Required
    private String hora;

    @Constraints.Required
    private String sentido;

//    static enum Orientacao {

    public Carona(Long id, Aluno criador, int vagas, int tolerancia, String data, String hora, String sentido) {
        this.id = id;
        this.criador = criador;
        this.vagas = vagas;
        this.tolerancia = tolerancia;
        this.data = data;
        this.hora = hora;
        this.sentido = sentido;
    }

    public Carona(Long id, Aluno criador, int tolerancia, String data, String hora, String sentido) {
        this.id = id;
        this.criador = criador;
        this.tolerancia = tolerancia;
        this.data = data;
        this.hora = hora;
        this.sentido = sentido;
    }

    public Carona(Long id, Aluno criador, int vagas, int tolerancia, String data, String hora) {
        this.id = id;
        this.criador = criador;
        this.vagas = vagas;
        this.tolerancia = tolerancia;
        this.data = data;
        this.hora = hora;
        this.sentido = sentido;
    }

//        @EnumValue("I") IDA,
//        @EnumValue("V") VOLTA
//    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public Aluno getCriador() {
        return criador;
    }

    public void setCriador(Aluno criador) {
        this.criador = criador;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public int getTolerancia() {
        return tolerancia;
    }

    public void setTolerancia(int tolerancia) {
        this.tolerancia = tolerancia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void decrementarVaga(){
        if(vagas > 0) {
            vagas -= 1;
        }
    }

}
