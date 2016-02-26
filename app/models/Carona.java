package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;
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
    private Orientacao sentido;

    static enum Orientacao {
        @EnumValue("I") IDA,
        @EnumValue("V") VOLTA
    }

    public Orientacao getSentido() {
        return sentido;
    }

    public void setSentido(Orientacao sentido) {
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

}
