package dk.kyuff.javafx.selectview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Date: 25/01/15
 * Time: 15.22
 */
public class Controller implements Initializable {
    @FXML
    SelectableTableView view;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn ageColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Person> data = createData();
        view.setItems(data);
    }

    private ObservableList<Person> createData() {
        Person hans = new Person("Hans", 67);
        Person kurt = new Person("Kurt", 54);
        Person ingeborg = new Person("Ingeborg", 11);
        Person vera = new Person("Vera", 6);
        return FXCollections.observableArrayList(hans, kurt, ingeborg, vera);
    }
}
