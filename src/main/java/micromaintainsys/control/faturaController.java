package micromaintainsys.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import micromaintainsys.dao.DAO;
import micromaintainsys.model.Fatura;
import micromaintainsys.model.SceneSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class faturaController implements Initializable {
    @FXML
    private AnchorPane faturaAnchor;

    @FXML
    private TableView<Fatura> tableView;
    @FXML
    private TableColumn<Fatura, Integer> idFaturaColumn;

    @FXML
    private TableColumn<Fatura, Integer> idOrdemColumn;

    @FXML
    private TableColumn<Fatura, Double> valorPagoColumn;

    @FXML
    private TableColumn<Fatura, Double> valorTotalColumn;

    @FXML
    void switchToOrdem() throws IOException {
        new SceneSwitch(faturaAnchor, "main.fxml");
    }
    @FXML
    void switchToTec() throws IOException {
        new SceneSwitch(faturaAnchor, "tecnicos.fxml");
    }
    @FXML
    void switchToClientes() throws IOException {
        new SceneSwitch(faturaAnchor, "clientes.fxml");
    }
    @FXML
    void switchToEstoque() throws IOException {
        new SceneSwitch(faturaAnchor, "estoque.fxml");
    }
    @FXML
    void switchToFatura() throws IOException {
        new SceneSwitch(faturaAnchor, "faturas.fxml");
    }
    @FXML
    void switchToFaturaGer() throws IOException {
        new SceneSwitch(faturaAnchor, "management_Scenes/fatura_ger.fxml");
    }
    @FXML
    void switchToOrdem_Compra() throws IOException {
        new SceneSwitch(faturaAnchor, "ordem_compra.fxml");
    }
    ObservableList<Fatura> observableFaturaList = FXCollections.observableArrayList(DAO.getFaturaDAO().pegaTodas());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.idFaturaColumn.setCellValueFactory(new PropertyValueFactory<>("faturaID"));
        this.idOrdemColumn.setCellValueFactory(new PropertyValueFactory<>("ordemID"));
        this.valorPagoColumn.setCellValueFactory(new PropertyValueFactory<>("valorPago"));
        this.valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        this.tableView.getItems().setAll(DAO.getFaturaDAO().pegaTodas());
        this.tableView.setItems(observableFaturaList);

    }

}
