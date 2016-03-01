package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Aluno extends Model {

    public static Finder<Long, Aluno> find = new Finder<Long, Aluno>(Aluno.class);

    @Id
    private Long id;

    @Constraints.Required
    private String nome;

    @Constraints.Required
    private String email;

    @Constraints.Required
    private String cidade;

    @Constraints.Required
    private String bairro;

    @Constraints.Required
    private String senha;

    @Constraints.Required
    private String matricula;

    @OneToMany(mappedBy = "criador", cascade= CascadeType.PERSIST)
    private Set<Carona> ofertadas;

    @OneToMany(cascade= CascadeType.PERSIST)
    private Set<Carona> pedidas;

    public void addOferta(Carona oferta){
        if (ofertadas == null) {
            ofertadas = new HashSet<Carona>();
        }
        oferta.setCriador(this);
        ofertadas.add(oferta);
    }

    public void addPedida(Carona pedida){
        if (pedidas == null) {
            pedidas = new HashSet<Carona>();
        }
        pedidas.add(pedida);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Set<Carona> getOfertadas() {
        return ofertadas;
    }

    public void setOfertadas(Set<Carona> ofertadas) {
        this.ofertadas = ofertadas;
    }

    public Set<Carona> getPedidas() {
        return pedidas;
    }

    public void setPedidas(Set<Carona> pedidas) {
        this.pedidas = pedidas;
    }

}
