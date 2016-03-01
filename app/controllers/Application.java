package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import models.Aluno;
import models.Carona;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    //private static GenericDAO dao = new GenericDAO();
    @Inject
    static EbeanServer ebeanServer = Ebean.getServer(null);

    public Result index() {
        List<Carona> caronas = ebeanServer.find(Carona.class).findList();
        return ok(views.html.index.render(caronas));
    }

    public Result sobre() {
        return ok(views.html.sobre.render());
    }

    public Result oferecerCarona() {
        return ok(views.html.oferecercarona.render());
    }

    public Result pedirCarona() {
        return ok(views.html.pedircarona.render());
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
            //return ok(views.html.index.render("OLÁ " + carona));
            List<Carona> caronas = ebeanServer.find(Carona.class).findList();
            return ok(views.html.index.render(caronas));
        }else {
            return ok("Problema ao logar!");
        }
    }

    public Result novoPedido() {
        if(getAlunoFromSession() != null){
            Form<Carona> caronaForm = form(Carona.class);
            Carona carona = caronaForm.bindFromRequest().get();
            carona.setVagas(-1);
            carona.setCriador(getAlunoFromSession());
            carona.save();
            List<Carona> caronas = ebeanServer.find(Carona.class).findList();
            return ok(views.html.index.render(caronas));
        }else {
            return ok("Problema ao logar!");
        }
    }

    public Result verCaronas() {
        List<Carona> caronas = ebeanServer.find(Carona.class).findList();

//        Collections.sort(anuncios, new Comparator<Carona>() {
//            public int compare(Carona a1, Carona a2) {
//                return a1.getData().getTime() < a2.getData().getTime() ? 1 : -1;
//            }
//        });
//        return ok(index.render(caronas));
        return ok(views.html.index.render(caronas));
    }

    public Result criarConta() {
        Form<Aluno> alunoForm = form(Aluno.class);
        Aluno aluno = alunoForm.bindFromRequest().get();
        aluno.save();
        logout();
        return ok(views.html.login.render());
    }

    public Result sair() {
        logout();
        return ok(views.html.login.render());
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
        logout();
        if(aluno != null){
            session("connected", String.valueOf(aluno.getId()));
            return ok(views.html.oferecercarona.render());
        }
        // mensagem de login errado
        return ok(views.html.oferecercarona.render());
    }

    public Result diminuirVagas(Long id) {
        Carona carona = ebeanServer.find(Carona.class).setId(id).findUnique();
        if (getAlunoFromSession().getCidade().equals(carona.getCriador().getCidade()) && getAlunoFromSession().getBairro().equals(carona.getCriador().getBairro())){
            carona.decrementarVaga();
            carona.save();
            return redirect(controllers.routes.Application.index());
        }
        //sem mudanças
        return  redirect(controllers.routes.Application.index());
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
