package micromaintainsys.control.management_Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import micromaintainsys.dao.DAO;
import micromaintainsys.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ResourceBundle;

import static micromaintainsys.utils.ViewUtils.*;


public class Ordens_GerController implements Initializable {

    @FXML
    private AnchorPane tecnicosAnchorPane;
    private Queue<Ordem> ordensAbertas;
    private ArrayList<Ordem> ordensServico;
    private ArrayList<Fatura> faturas;
    private Estoque estoque;
    private Tecnico tecnicoSessao;
    private int objID;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button UpdateButton1;

    @FXML
    private Button addTecButton;

    @FXML
    private ImageView closeButton;

    @FXML
    private TextField idField;

    @FXML
    private TextField idRemoveField1;

    @FXML
    private Button searchButton;

    @FXML
    private Button servicosButton;

    @FXML
    private Button servicosPageButtonUp;

    @FXML
    private ChoiceBox<StatusOrdem> statusChoice;

    @FXML
    private Button tecButton;

    @FXML
    private TextField tecField;
    @FXML
    private TextField idSearchField;
    @FXML
    private TextField tecFieldUpdate;


    @FXML
    void addOrdem() throws IOException {
        if (idField.getText().isEmpty()){
            showWarningAlert("Campo vazio", "Para prosseguir, preencha todos os campos");
        }
        else if (DAO.getClienteDAO().pegaPorId(Integer.parseInt(idField.getText())) == null){
            showErrorAlert("Cliente não encontrado", "Por favor, insira um ID válido");
        }
        else{
            int id = Integer.parseInt(idField.getText());
            Cliente cliente = DAO.getClienteDAO().pegaPorId(id);
            Ordem ordem = new Ordem(cliente.getId());
            DAO.getOrdemDAO().cria(ordem.getClienteID());
            new SceneSwitch(tecnicosAnchorPane, "main.fxml", tecnicoSessao, objID);

        }

    }

    @FXML
    void searchOrder(){
        String id = idSearchField.getText();
        if (id.isEmpty() || DAO.getOrdemDAO().pegaPorId(Integer.parseInt(id)) == null){
            showErrorAlert("Campo vazio ou ID inválido", "Por favor, verifique o campo de ID");
        }
        else{
            fillUpdateFields();
        }
    }


    void fillUpdateFields(){
        int id = Integer.parseInt(idSearchField.getText());
        Ordem ordem = DAO.getOrdemDAO().pegaPorId(id);
        if (ordem.getTecnicoID() == 0){
            tecFieldUpdate.setText("0");
        }
        else {
            Tecnico tecnico = DAO.getTecnicoDAO().pegaPorId(ordem.getTecnicoID());
            tecFieldUpdate.setText(String.valueOf(tecnico.getTecnicoID()));
        }
        statusChoice.setValue(ordem.getStatus());
    }

    @FXML
    void clearUpdateFields(){
        tecFieldUpdate.clear();
        statusChoice.setValue(null);

    }
    @FXML
    void updateOrder(){
        String idText = idSearchField.getText();
        String tecText = tecFieldUpdate.getText();
        StatusOrdem status = statusChoice.getValue();
        if (idText.isEmpty() || tecText.isEmpty() || status == null){
            showWarningAlert("Campo vazio", "Para prosseguir, preencha todos os campos");
        }
        else if (DAO.getOrdemDAO().pegaPorId(Integer.parseInt(idText)) == null){
            showErrorAlert("Ordem não encontrada", "Por favor, insira um ID válido");
        }
        else if (DAO.getTecnicoDAO().pegaPorId(Integer.parseInt(tecText)) == null){
            showErrorAlert("Técnico não encontrado", "Por favor, insira um ID válido");
        }
        else{
            int ordem_id = Integer.parseInt(idText);
            Ordem ordem = DAO.getOrdemDAO().pegaPorId(ordem_id);
            ordem.setStatus(status);
            int tecnico_id = Integer.parseInt(tecText);
            /*TODO Fazer as verificações de atribuição de ordem do antigo mainController*/
            if (tecnico_id != ordem.getTecnicoID()){
                ordem.setTecnicoID(tecnico_id);
                Tecnico tecnico = DAO.getTecnicoDAO().pegaPorId(tecnico_id);
                tecnico.setOrdemEmAndamentoID(ordem_id);
            }

            DAO.getOrdemDAO().atualiza(ordem);
            showInformationAlert("Ordem atualizada", "A ordem foi atualizada com sucesso");
        }
    }
    @FXML
    void removeOrder(){
        String idText = idRemoveField1.getText();
        if (idText.isEmpty() || DAO.getOrdemDAO().pegaPorId(Integer.parseInt(idText)) == null){
            showErrorAlert("Ordem não encontrada", "Por favor, insira um ID válido");
        }
        else{
            DAO.getOrdemDAO().remove(Integer.parseInt(idText));
            showInformationAlert("Ordem removida", "A ordem foi removida com sucesso");
        }
    }

    @FXML
    void switchToOrdem() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "main.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToServicos() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "management_Scenes/servicos_ger.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToTec() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "tecnicos.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToClientes() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "clientes.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToEstoque() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "estoque.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToFatura() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "faturas.fxml", tecnicoSessao, objID);
    }
    @FXML
    void switchToOrdem_Compra() throws IOException {
        new SceneSwitch(tecnicosAnchorPane, "ordem_compra.fxml", tecnicoSessao, objID);
    }
    public void setTecnicoSessao(Tecnico tecnicoSessao) {
        this.tecnicoSessao = tecnicoSessao;
    }
    @FXML
    void logoutTecnico() throws IOException {
        if (this.tecnicoSessao != null){
            this.tecnicoSessao = null;
            showInformationAlert("Logout efetuado", "Logout efetuado com sucesso");
            new SceneSwitch(tecnicosAnchorPane, "login.fxml", tecnicoSessao, objID);
        }
        else{
            showErrorAlert("Erro ao fazer logout", "Não há nenhum técnico logado no sistema");
        }
        new SceneSwitch(tecnicosAnchorPane, "login.fxml", tecnicoSessao, objID);
    }

    ObservableList<Tecnico> observableList = FXCollections.observableArrayList(DAO.getTecnicoDAO().pegaTodos());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar o filtro de entrada para aceitar somente números
            numberFilter(idField);
            numberFilter(idRemoveField1);
            numberFilter(tecFieldUpdate);
            numberFilter(idSearchField);

//            numberFilter(OrderFieldUpdate);

        ObservableList<StatusOrdem> statusOrdemObservableList = FXCollections.observableArrayList(StatusOrdem.values());
        statusChoice.setItems(statusOrdemObservableList);
        statusChoice.getSelectionModel().selectFirst();




    }
}
