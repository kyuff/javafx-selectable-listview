package dk.kyuff.javafx.selectview;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

/**
 * User: swi
 * Date: 24/01/15
 * Time: 11.16
 */
public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Selectable TableView");

        SelectableTableView<Person> view = new SelectableTableView<>();

        view.markedProperty().addListener((ListChangeListener<Person>) c -> {
            System.out.println(c.getList());
            if( c.getList().size() > 1) {
                view.checkboxColumnVisibleProperty().set(false);
            }
        });


        Person hans = new Person("Hans", 67);
        Person kurt = new Person("Kurt", 54);
        Person ingeborg = new Person("Ingeborg", 11);
        Person vera = new Person("Vera", 6);
        ObservableList<Person> data = FXCollections.observableArrayList(hans, kurt, ingeborg);
        view.markedProperty().add(ingeborg);
        view.setItems(data);

        view.getItems().add(vera);

        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));

        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getAge()));

        view.getColumns().addAll(nameColumn, ageColumn);

        primaryStage.setScene(new Scene(view, 500, 500));
        primaryStage.show();

        view.getItems().remove(kurt);
        view.markedProperty().add(vera);

        System.out.println("Yo!");

    }

}
