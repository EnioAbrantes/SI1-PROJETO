package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import models.Aluno;
import models.Carona;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

import static play.data.Form.form;

public class Application extends Controller {

    //private static GenericDAO dao = new GenericDAO();
    @Inject
    static EbeanServer ebeanServer = Ebean.getServer(null);
    static List<Carona> meusPedidos = new LinkedList<Carona>();
    static List<Carona> minhasOfertas = new LinkedList<Carona>();

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
            minhasOfertas(carona);
            List<Carona> caronas = ebeanServer.find(Carona.class).findList();
            return ok(views.html.index.render(caronas));
        }else {
            return ok(views.html.login.render("Você precisa entrar em sua conta para realizar uma oferta de carona!"));
        }
    }

    public Result novoPedido() {
        if(getAlunoFromSession() != null){
            Form<Carona> caronaForm = form(Carona.class);
            Carona carona = caronaForm.bindFromRequest().get();
            carona.setVagas(-1);
            carona.setCriador(getAlunoFromSession());
            carona.save();
            meusPedidos(carona);
            List<Carona> caronas = ebeanServer.find(Carona.class).findList();
            return ok(views.html.index.render(caronas));
        }else {
            return ok(views.html.login.render("Você precisa entrar em sua conta para realizar um pedido de carona!"));
        }
    }
    //arrumar isso
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
        return ok(views.html.login.render("Entre em sua conta"));
    }

    public Result sair() {
        logout();
        return ok(views.html.login.render("Entre em sua conta"));
    }

    public Result login() {
        return ok(views.html.login.render("Entre em sua conta"));
    }

    public Result getPedidos() {
        List<Carona>  pedidos = new LinkedList<Carona>();
        for (Carona meuPedido : meusPedidos){
            try {
                if (getAlunoFromSession().getEmail().equals(meuPedido.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(meuPedido.getCriador().getEmail())) {
                    pedidos.add(meuPedido);
                }
            }catch(Exception e){
                return ok(views.html.login.render("Você precisa entrar em sua conta para ver seus pedidos de carona!"));
            }
        }
        return ok(views.html.meuspedidos.render(pedidos));
    }

    public Result getOfertas() {
        List<Carona>  ofertas = new LinkedList<Carona>();
        for (Carona minhaOferta : minhasOfertas){
            try {
                if (getAlunoFromSession().getEmail().equals(minhaOferta.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(minhaOferta.getCriador().getEmail())){
                    ofertas.add(minhaOferta);
                }
            }catch(Exception e){
                return ok(views.html.login.render("Você precisa entrar em sua conta para ver seus pedidos de carona!"));
            }
        }
        return ok(views.html.minhasofertas.render(ofertas));
    }

    public void meusPedidos(Carona carona) {
        if(getAlunoFromSession() != null){
            if(getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail())){
                meusPedidos.add(carona);
            }
        }
    }


    public void minhasOfertas(Carona carona) {
        if(getAlunoFromSession() != null){
            if(getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail())){
                minhasOfertas.add(carona);
            }
        }
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

        return ok(views.html.login.render("Email incorreto ou senha incorreta"));
    }

    public Result diminuirVagas(Long id) {
        Carona carona = ebeanServer.find(Carona.class).setId(id).findUnique();
        try {
            if (getAlunoFromSession().getCidade().equals(carona.getCriador().getCidade()) && getAlunoFromSession().getBairro().equals(carona.getCriador().getBairro()) && carona.getVagas() > -1) {
                carona.decrementarVaga();
                carona.save();
                return redirect(controllers.routes.Application.index());
            }
            if (getAlunoFromSession().getCidade().equals(carona.getCriador().getCidade()) && getAlunoFromSession().getBairro().equals(carona.getCriador().getBairro()) && carona.getVagas() == -1) {
                carona.setVagas(-2);
                carona.save();
                return redirect(controllers.routes.Application.index());
            }
        }catch (Exception e){
            return ok(views.html.login.render(""));
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
