package com.example.restaurant;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;

class CustomerMenu extends RestaurantScene {
    private final ArrayList<ListView<Dish>> views;
    private final TabPane categoryTabs;
    private ArrayList<FilteredList<Dish>> filteredLists;

    private final Pane layout;

    public CustomerMenu(RestaurantDB restaurantDB) {

        this.categoryTabs = new TabPane();

        this.filteredLists = new ArrayList<>();

        this.views = new ArrayList<>();

        Arrays.stream(DishType.values()).toList().forEach(dishType -> {
            FilteredList<Dish> newFilteredList = new FilteredList<>(restaurantDB.getDishes());
            newFilteredList.setPredicate(dish -> dish.getCategory() == dishType);
            this.filteredLists.add(dishType.ordinal(), newFilteredList);

            ListView<Dish> newView = new ListView<>(newFilteredList);
            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.views.add(newView);

            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            newView.setCellFactory(new Callback<>() {

                /* Overrides the call method to custom-define how cells are created */
                @Override
                public ListCell<Dish> call(ListView<Dish> dishListView) {
                    /* Creates a new ListCell that displays a formatted string for the dish*/
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(Dish item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || item == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                Text content = new Text(item.getDname() + " • "
                                        + "$" + item.getPrice() +
                                        "\n" + item.getDescription()
                                );

                                content.wrappingWidthProperty().bind(newView.widthProperty().add(-40));

                                setWrapText(true);

                                setGraphic(content);
                            }
                        }
                    };
                }
            });

            String categoryString = dishType.toString();
            Tab newTab = new Tab(categoryString.charAt(0) + categoryString.substring(1).toLowerCase() + "s");
            newTab.setContent(newView);
            this.categoryTabs.getTabs().add(newTab);
        });

        this.categoryTabs.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab, t1) -> {
                    int categoryIndex = categoryTabs.getTabs().indexOf(tab);
                    views.get(categoryIndex).getSelectionModel().clearSelection();
                }
        );

        layout = new VBox();
        layout.getChildren().add(this.categoryTabs);
    }

    /* Returns the root element for the menu view, allowing it to be
     * embedded in a layout */
    public Pane getAsElement() {
        return this.layout;
    }
}
