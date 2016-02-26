package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import models.Aluno;
import models.Carona;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

import static play.data.Form.form;

public class Application extends Controller {

    //private static GenericDAO dao = new GenericDAO();
    @Inject
    static EbeanServer ebeanServer = Ebean.getServer(null);

    public Result index() {
        return ok(views.html.index.render(""));
    }

    public Result sobre() {
        return ok();
    }

    public Result oferecerCarona() {
        return ok(views.html.oferecercarona.render());
    }

    public Result cadastro() {
        return ok(views.html.cadastro.render());
    }
    public Result verOfertas() {
        return ok();
    }

    public Result novaCarona() {
        if(getAlunoFromSession() != null){
            Form<Carona> caronaForm = form(Carona.class);
            Carona carona = caronaForm.bindFromRequest().get();
            carona.setCriador(getAlunoFromSession());
            carona.save();
                return ok(views.html.index.render("OLÁ " + carona.getHora()));
        }else {
            return ok("Problema ao logar!");
        }
    }
    public Result criarConta() {
        Form<Aluno> alunoForm = form(Aluno.class);
        Aluno aluno = alunoForm.bindFromRequest().get();
        aluno.save();
        return ok(views.html.index.render("OLÁ " + aluno.getBairro()));
    }

    public Result login() {
        return ok(views.html.login.render());
    }

    public Result logar() {
        String email = form().bindFromRequest().get("email");
        String senha = form().bindFromRequest().get("senha");
        Aluno aluno = ebeanServer.find(Aluno.class)
                .select("email,senha")
                .where().ieq("email",email).eq("senha",senha)
                .findUnique();
        if(aluno != null){
            session("connected", String.valueOf(aluno.getId()));
            return ok(views.html.index.render("OLÁ " + aluno));
        }
        return ok(views.html.index.render("Deu merda "));
    }

    public Result logout() {
        session().clear();
        return ok("Bye");
    }

    private Aluno getAlunoFromSession(){
        String userID = session("connected");
        if(userID != null) {
            return Aluno.find.byId(Long.valueOf(userID));
        } else {
            return null;
        }
    }

}
