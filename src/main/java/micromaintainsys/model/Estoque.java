package micromaintainsys.model;

import java.util.ArrayList;
import java.util.Hashtable;

public class Estoque {
    private Hashtable<String, Integer> pecas;
    private ArrayList<OrdemCompra> ordensCompra;

    public Estoque() {
        pecas = new Hashtable<String,Integer>();
        ordensCompra = new ArrayList<>();
    }

    public Estoque(Hashtable<String, Integer> pecas, ArrayList<OrdemCompra> ordensCompra) {
        this.pecas = pecas;
        this.ordensCompra = ordensCompra;
    }

    public void adicionaPeca(String peca, int quantidade) {
        peca = peca.toLowerCase();
        if (!pecas.containsKey(peca)){
            pecas.put(peca, quantidade);
        }
        else{
            pecas.replace(peca, pecas.get(peca) + quantidade);
        }
    }
    public void removePeca(String peca, int quantidade) {
        peca = peca.toLowerCase();
        /*Não pode tirar mais que o que tem estoque*/
        if (quantidade > pecas.get(peca))
            quantidade = pecas.get(peca);
        pecas.replace(peca, pecas.get(peca) -quantidade);
    }

    public void deletaPeca(String peca){
        pecas.remove(peca);
    }
    public int pegaEstoqueDePeca(String peca) {
        peca = peca.toLowerCase();
        return this.pecas.get(peca);
    }
    public void criaOrdemCompra(OrdemCompra ordemCompra) {
        ordensCompra.add(ordemCompra);
    }
    public void removeOrdemCompra(OrdemCompra ordemCompra) {
        ordensCompra.remove(ordemCompra);
    }
    public ArrayList<OrdemCompra> verificaOrdensCompra() {
        return ordensCompra;
    }
}
