package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;

class CustomerMenu
{
    private Stage stage;
    private ArrayList<ListView<Dish>> views;
    private ObservableList<Dish> dishes;
    private TabPane categoryTabs;
    private ArrayList<FilteredList<Dish>> filteredLists;
    private Scene scene;

    public CustomerMenu(RestaurantDB restaurantDB, Stage stage)
    {
        this.stage = stage;

        this.categoryTabs = new TabPane();

        this.dishes = FXCollections.observableList(restaurantDB.getDishes());

        this.categoryTabs.prefWidthProperty().bind(stage.widthProperty());
        this.categoryTabs.prefHeightProperty().bind(stage.heightProperty());

        this.filteredLists = new ArrayList<>();

        this.views = new ArrayList<>();

        Arrays.stream(DishType.values()).toList().forEach(dishType -> {
            FilteredList<Dish> newFilteredList = new FilteredList<Dish>(this.dishes);
            newFilteredList.setPredicate(dish -> dish.getCategory() == dishType);
            this.filteredLists.add(dishType.ordinal(), newFilteredList);

            ListView<Dish> newView = new ListView<>(newFilteredList);
            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.views.add(newView);

            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            newView.setCellFactory(new Callback<ListView<Dish>, ListCell<Dish>>() {

                /* Overrides the call method to custom-define how cells are created */
                @Override
                public ListCell<Dish> call(ListView<Dish> dishListView) {
                    /* Creates a new ListCell that displays a formatted string for the dish*/
                    return new ListCell<Dish>() {
                        @Override
                        protected void updateItem(Dish item, boolean empty)
                        {
                            super.updateItem(item, empty);

                            if(empty || item == null)
                            {
                                setText(null);
                                setGraphic(null);
                            }
                            else
                            {
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

        VBox layout = new VBox();
        layout.getChildren().add(this.categoryTabs);

        this.scene = new Scene(layout, RestaurantScene.xDim, RestaurantScene.yDim);
    }

    /* Swaps to the menu view on the stage provided */
    public void display() {
        stage.setScene(this.scene);
        stage.show();
    }
}