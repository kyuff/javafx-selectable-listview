package dk.kyuff.javafx.selectview;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 24/01/15
 * Time: 11.20
 */
public class SelectableTableView<T> extends TableView<T> {

    private Map<T, BooleanProperty> selectMap;

    public SelectableTableView() {
        super();
        selectMap = new HashMap<>();
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        bindItemsChangeListener();
        initCheckBoxColumn();
        initKeyboardEvents();
        itemsProperty().addListener((observable, oldValue, newValue) -> {
            // this is required because a completely new dataset is provided
            // all markings must be invalidated
            invalidateMarkings();
        });
    }

    /**
     * Must clear all state. This method is called when completely new data is provided to the view
     */
    private void invalidateMarkings() {
        markedProperty().clear();
        selectMap.clear();
        bindItemsChangeListener();
    }

    /**
     * Create a new listener on the items in the view, so removed items can be cleared from
     * internal state of this class.
     */
    private void bindItemsChangeListener() {
        getItems().addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(removed -> {
                        selectMap.remove(removed);
                        markedProperty().remove(removed);
                    });
                }
            }
        });
    }

    /**
     * Keyboard bindings
     */
    private void initKeyboardEvents() {
        this.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
    }

    private void handleMouseClicked(MouseEvent event) {
        T item = getSelectionModel().getSelectedItem();
        if (item == null) {
            return;
        }
        if (MouseButton.PRIMARY == event.getButton()) {
            if (event.getClickCount() == 1) {
                onMouseSingleClick(item);
            }
            if (event.getClickCount() == 2) {
                onMouseDoubleClick(item);
            }
        }
    }

    /**
     * Should possible be made public, or changed into an ObjectProperty that can contain
     * an event handler.
     *
     * @param item the item that is clicked on
     */
    private void onMouseDoubleClick(T item) {
        System.out.println("double click: " + item);
    }

    /**
     * Should possible be made public, or changed into an ObjectProperty that can contain
     * an event handler.
     *
     * @param item the item that is clicked on
     */
    private void onMouseSingleClick(T item) {
        System.out.println("single click: " + item);
    }

    private void handleKeyPressed(KeyEvent event) {
        Collection<T> items = getSelectionModel().getSelectedItems();
        if (items == null || items.isEmpty()) {
            return;
        }
        switch (event.getCode()) {
            case SPACE:
                onSpacePressed(items);
                break;
            case ENTER:
                onEnterPressed(items);
                break;
        }
    }

    private void onSpacePressed(Collection<T> items) {
        items.forEach(this::toggleMark);
    }

    private void onEnterPressed(Collection<T> items) {

    }

    /**
     * Creates the column with the checkboxes in. Makes sure to populate the selectMap
     * with items added and a select property for the checkbox.
     */
    private void initCheckBoxColumn() {
        TableColumn<T, T> checkboxColumn = new TableColumn<>();

        checkboxColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue()));

        checkboxColumn.setCellFactory(new Callback<TableColumn<T, T>, TableCell<T, T>>() {
            @Override
            public TableCell<T, T> call(TableColumn<T, T> param) {
                return new TableCell<T, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            CheckBox checkBox = new CheckBox();
                            selectMap.put(item, checkBox.selectedProperty());
                            // important to do before adding the listener below
                            boolean selected = markedProperty().contains(item);
                            checkBox.setSelected(selected);
                            checkBox.selectedProperty().addListener((observable, oldValue, marked) -> {
                                if (marked) {
                                    markedProperty().add(item);
                                } else {
                                    markedProperty().remove(item);
                                }
                            });
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });
        checkboxColumn.setStyle("checkbox");
        checkboxColumn.setMinWidth(80.0);
        getColumns().add(0, checkboxColumn);
    }

    /**
     * Toggles between marked/unmarked for a given item.
     * This method could possible be made public and get a few
     * brothers in the form of mark/unmark methods.
     *
     * @param item that should be toggled
     */
    private void toggleMark(T item) {
        boolean marked = markedProperty().contains(item);
        selectMap.get(item).set(!marked);
    }

    /**
     * Observablelist containing all the items that is currently marked.
     */
    private ObservableList<T> marked;


    /**
     * Use this property to mark or unmark items in the view
     *
     * @return observable list of all marked items in the view.
     */
    public ObservableList<T> markedProperty() {
        if (marked == null) {
            marked = FXCollections.observableArrayList();
        }
        return marked;
    }

    public BooleanProperty checkboxColumnVisibleProperty() {
        return getColumns().get(0).visibleProperty();
    }


}
