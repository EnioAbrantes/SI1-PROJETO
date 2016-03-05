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

    @Inject
    static EbeanServer ebeanServer = Ebean.getServer(null);
    static List<Carona> meusPedidos = new LinkedList<Carona>();
    static List<Carona> minhasOfertas = new LinkedList<Carona>();

    /**
     * Janela inicial
     * @return
     */
    public Result index() {
        return verCaronas();
    }

    /**
     * Chamada para a janela sobre informações da aplicação presente no menu principal
     * @return
     */
    public Result sobre() {
        return ok(views.html.sobre.render());
    }

    /**
     * Chamada para a janela de oferecer caronas presente no menu principal
     * @return
     */
    public Result oferecerCarona() {
        return ok(views.html.oferecercarona.render());
    }

    /**
     * Chamada para a janela de pedir caronas presente no menu principal
     * @return
     */
    public Result pedirCarona() {
        return ok(views.html.pedircarona.render());
    }

    /**
     * Chamada para a janela de cadastro presente no menu principal
     * @return
     */
    public Result cadastro() {
        return ok(views.html.cadastro.render());
    }

    /**
     * Inserindo uma nova oferta de carona no banco de dados a partir e informações recebidas pelo usuário
     * @return
     */
    public Result novaCarona() {
        if(getAlunoFromSession() != null){
            Form<Carona> caronaForm = form(Carona.class);
            Carona carona = caronaForm.bindFromRequest().get();
            carona.setCriador(getAlunoFromSession());
            carona.save();
            minhasOfertas(carona);
            return verCaronas();
        }else {
            return ok(views.html.login.render("Você precisa entrar em sua conta para realizar uma oferta de carona!"));
        }
    }

    /**
     * Inserindo um novo pedido de carona no banco de dados a partir e informações recebidas pelo usuário
     * @return
     */
    public Result novoPedido() {
        if(getAlunoFromSession() != null){
            Form<Carona> caronaForm = form(Carona.class);
            Carona carona = caronaForm.bindFromRequest().get();
            carona.setVagas(-1);
            carona.setCriador(getAlunoFromSession());
            carona.save();
            meusPedidos(carona);
            return verCaronas();
        }else {
            return ok(views.html.login.render("Você precisa entrar em sua conta para realizar um pedido de carona!"));
        }
    }

    /**
     * Exibição de todas as caronas presentes no banco de dados
     * @return
     */
    public Result verCaronas() {
        List<Carona> caronas = ebeanServer.find(Carona.class).findList();
        return ok(views.html.index.render(caronas));
    }

    /**
     * Cadastrando usuário no banco de dadoos a partir dos dados inseridos no cadastrar
     * @return
     */
    public Result criarConta() {
        Form<Aluno> alunoForm = form(Aluno.class);
        Aluno aluno = alunoForm.bindFromRequest().get();
        aluno.save();
        logout();
        return ok(views.html.login.render("Entre em sua conta"));
    }

    /**
     * logout de um usuário
     * @return
     */
    public Result sair() {
        logout();
        return ok(views.html.login.render("Entre em sua conta"));
    }

    /**
     * Chamada para a janela de Entrar presente no menu principal
     * @return
     */
    public Result login() {
        return ok(views.html.login.render("Entre em sua conta"));
    }

    /**
     * Método responsável por retornar todos os pedidos de um determinado usuário
     * @return
     */
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

    /**
     * Método responsável por retornar todas as ofertas de um determinado usuário
     * @return
     */
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

    /**
     * Método responsável por adicionar na lista do usuário seu pedido
     * @param carona
     */
    public void meusPedidos(Carona carona) {
        if(getAlunoFromSession() != null){
            if(getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail())){
                meusPedidos.add(carona);
            }
        }
    }

    /**
     * Método responsável por adicionar na lista do usuário sua oferta
     * @param carona
     */
    public void minhasOfertas(Carona carona) {
        if(getAlunoFromSession() != null){
            if(getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail()) && getAlunoFromSession().getEmail().equals(carona.getCriador().getEmail())){
                minhasOfertas.add(carona);
            }
        }
    }

    /**
     * Método responsável por verificar se o usuário que está tentando entrar está cadastrado ou não
     * @return
     */
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

    /**
     * Método responsável por decrementar o número de vagas ofertadas se for maior que 0
     * @param id
     * @return
     */
    public Result diminuirVagas(Long id) {
        Carona carona = ebeanServer.find(Carona.class).setId(id).findUnique();
        try {
            if(getAlunoFromSession().getCidade().equals(carona.getCriador().getCidade()) && getAlunoFromSession().getBairro().equals(carona.getCriador().getBairro())){
                if (carona.getVagas() > -1){
                    carona.decrementarVaga();
                    carona.save();
                    return redirect(controllers.routes.Application.index());
                }else{
                    carona.setVagas(-2);
                    carona.save();
                    return redirect(controllers.routes.Application.index());
                }
            }
        }catch (Exception e){
            return ok(views.html.login.render("Por favor , entre em sua conta!"));
        }
        //sem mudanças
        return  redirect(controllers.routes.Application.index());
    }

    /**
     * Método responsável por desconectar da conta atualmente conectada
     * @return
     */
    public Result logout() {
        session().clear();
        return ok("Bye");
    }

    /**
     * Método responsável por armazenar a sessão do usuário
     * @return
     */
    private Aluno getAlunoFromSession(){
        String userID = session("connected");
        if(userID != null) {
            return Aluno.find.byId(Long.valueOf(userID));
        } else {
            return null;
        }
    }

}
