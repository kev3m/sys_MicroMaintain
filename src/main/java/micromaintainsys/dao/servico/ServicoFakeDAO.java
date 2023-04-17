package micromaintainsys.dao.servico;

import micromaintainsys.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
/**
 * Classe que implementa a interface InterfaceServico e fornece uma implementação para as operações de
 * acesso a dados relacionados aos servicos.
 */
public class ServicoFakeDAO implements InterfaceServico {
    /**
     * Hashtable que armazena os Servicos criados
     */
    static Hashtable<Integer, Servico> servicosCadastrados = new Hashtable<>();
    /**
     Contador estático usado para gerar IDs únicos para cada novo servico criado.
     */
    private static int idCounter = 0;
    /**
     * Cria um novo Servico e o adiciona em Servicos cadastrados
     *
     * @param categoriaServico Categoria do Servico
     * @param valor Valor do Servico
     * @param peca Peça associada ao Servico
     * @param descricao Descrição do Servico
     * @param ordemID ID da Ordem de Serviço associada ao Servico
     * @return Novo Servico criado
     */
    public Servico cria(CategoriaServico categoriaServico, double valor, String peca, String descricao, int ordemID){
        Servico novoServico = new Servico(categoriaServico, valor, peca, descricao, ordemID);
        novoServico.setServicoID(idCounter);
        servicosCadastrados.put(idCounter, novoServico);
        idCounter++;
        return novoServico;
    }

    /**
     * Retorna os serviços cadastros no ID da Ordem especificada
     * @param ordemId ID da Ordem
     * @return Servicos com o ID da Ordem especificada, ou null caso não exista
     */
    public Servico pegaPorId(int ordemId) {return servicosCadastrados.get(ordemId);}
    /**
     * Atualiza um Servico existente em Servicos cadastrados
     * @param servico Servico a ser atualizado
     * @return True se a atualização foi bem sucedida, false caso contrário
     */
    public boolean atualiza(Servico servico){
        return true;
    }
    /**
     * Remove o Servico com o ID especificado em Servicos cadastrados
     * @param servicoId ID do Servico a ser removido
     * @return true se a remoção foi bem sucedida, false caso contrário
     */
    public boolean remove(int servicoId) {
        Servico result = servicosCadastrados.remove(servicoId);
        if (result != null){
            return true;
        }
        return false;
    }
    /**
     * Retorna todos os Servicos associados a uma determinada Ordem de Serviço
     * @param ordemID ID da Ordem de Serviço
     * @return Todos os Servicos associados a Ordem de Serviço especificada
     */
    public ArrayList<Servico> pegaTodosPorOrdemID(int ordemID){
        ArrayList<Servico> servicos = new ArrayList<Servico>();
        Enumeration<Servico> servicosEnum = this.servicosCadastrados.elements();
        while (servicosEnum.hasMoreElements()){
            Servico servico = servicosEnum.nextElement();
            if (servico.getOrdemID() == ordemID){
                servicos.add(servico);
            }
        }
        return servicos;
    }
    /**
     * Retorna todos os serviços criados entre duas datas específicas.
     *
     * @param inicio Data de início do intervalo desejado.
     * @param fim Data de término do intervalo desejado.
     * @return Todos os serviços criados entre as datas especificadas.
     */
    public ArrayList<Servico> pegaTodosPorDataCriacao(Calendar inicio, Calendar fim){
        ArrayList<Servico> servicos = new ArrayList<>();
        for (Servico servico: this.servicosCadastrados.values()){
            Calendar abertura = servico.getHorarioAbertura();
            if (abertura.after(inicio) && abertura.before(fim)){
                servicos.add(servico);
            }
        }
        return servicos;
    }
    /**
     Reseta o contador de IDs dos servicos para zero.
     */
    public void resetIDCounter(){
        idCounter = 0;
    }
}