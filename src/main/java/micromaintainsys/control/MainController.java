package micromaintainsys.control;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;

import micromaintainsys.dao.DAO;
import micromaintainsys.model.*;

/**
 * Controller responsável por prover a interface com o sistema.
 */
public class MainController {
    private Queue<Ordem> ordensAbertas;
    private ArrayList<Ordem> ordensCanceladas;
    private ArrayList<Ordem> ordensFinalizadas;
    private ArrayList<OrdemCompra> ordensCompras;
    private ArrayList<Fatura> faturas;
    //Armazena o ID do tecnico logado no sistema
    private Tecnico tecnicoSessao;

    /*
    Métodos relacionados a TÉCNICOS
    */
    /**
     * Cria um novo técnico.
     * @param nome nome do técnico
     * @param senha senha do técnico
     * @return id do novo técnico
     */
    public Tecnico criaTecnico(String nome, String senha){
        return DAO.getTecnicoDAO().cria(nome, senha);
    }

    /**
     * Realiza o login de um técnico.
     * @param id id do técnico
     * @param senha senha do técnico
     * @return  true: login realizado com sucesso, false: não foi possível realizar o login
     */
    public boolean loginTecnico(int id, String senha){
        /*Não é possível fazer login sem fazer logoff do técnico anterior!*/
        Tecnico loginTecnico = DAO.getTecnicoDAO().pegaPorId(id);
        if (this.tecnicoSessao == null
                && loginTecnico != null
                && DAO.getTecnicoDAO().autentica(id, senha)){
            this.tecnicoSessao = loginTecnico;
            return true;
        }
        return false;
    }

    /**
     * Realiza o log out do técnico.
     */
    public void logoutTecnico(){
        this.tecnicoSessao = null;
    }

    /**
     * Remove um técnico do sistema.
     * @param tecnicoID id do técnico a ser removido
     * @return
     */
    public boolean removeTecnico(int tecnicoID){
        if (tecnicoID >= 0 && DAO.getTecnicoDAO().remove(tecnicoID)){
            return true;
        }
        return false;
    }

    /*
    Métodos relacionados a CLIENTES
     */

    /**
     * Cria um novo cliente.
     * @param nome nome do cliente
     * @param endereco endereço do cliente
     * @param telefone telefone do cliente
     * @return objeto do novo cliente
     */
    public Cliente criaCliente(String nome, String endereco, String telefone){
        return DAO.getClienteDAO().cria(nome, endereco, telefone);
    }

    /**
     * Remove um cliente.
     * @param clienteID id do cliente
     * @return
     */
    public boolean removeCliente(int clienteID){
        if (clienteID >= 0 && DAO.getClienteDAO().remove(clienteID)){
            return true;
        }
        return false;
    }

    /*
    Métodos relacionados a Ordens
     */
    public Ordem criaOrdem(int clienteID){
        Ordem novaOrdem = DAO.getOrdemDAO().cria(clienteID);
        this.ordensAbertas.add(novaOrdem);
        return novaOrdem;
    }

    public boolean atribuiOrdem(Ordem ordem, int tecnicoID){
        Tecnico tecnico = DAO.getTecnicoDAO().pegaPorId(tecnicoID);
        /*TODO criar uma exception p/ lidar com isso!*/
        if (tecnico.temOrdemEmAberto())
            return false;
        else{
            tecnico.cadastraOrdem(ordem);
            ordem.setTecnicoID(tecnicoID);
            DAO.getTecnicoDAO().atualiza(tecnico);
            DAO.getOrdemDAO().atualiza(ordem);
            return true;
        }
    }

    /**
     * Gera a fatura de uma ordem com o valor igual
     * ao da soma dos serviços cadastrados na ordem
     * @param ordem
     * @return
     */
    public Fatura geraFatura(Ordem ordem){
        if (ordem.getFaturaID() > -1){
            return null;
        }
        int ordemID = ordem.getOrdemID();

        ArrayList<Servico> servicosOrdem = DAO.getServicoDAO().pegaTodosPorOrdemID(ordemID);
        double valotTotal = servicosOrdem.stream().mapToDouble(Servico::getValor).sum();
        Fatura fatura = DAO.getFaturaDAO().cria(ordemID, valotTotal);
        ordem.setFaturaID(fatura.getFaturaID());
        return fatura;
    }
    public Servico criaServico(CategoriaServico categoria, double valor, Peca peca, String descricao, int ordemID ){
        return DAO.getServicoDAO().cria(categoria, valor, peca, descricao, ordemID);
    }

    public Pagamento realizaPagamento(TipoPagamento tipo, double valor, int faturaID){
        Fatura fatura = DAO.getFaturaDAO().pegaPorId(faturaID);
        /*TODO tratar caso de o pagamento ser mais que o necessário (vai ter função troco?)*/
        //double restante = fatura.getValorTotal() - fatura.getValorPago();

        fatura.setValorPago(fatura.getValorPago() + valor);
        Pagamento novoPagamento = DAO.getPagamentoDAO().cria(tipo, valor, faturaID);
        return novoPagamento;
    }

}
