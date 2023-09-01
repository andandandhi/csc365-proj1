package com.example.restaurant;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public abstract class RestaurantScene {

     public static final int xDim = 600;
     public static final int yDim = 500;

     public abstract Parent getAsElement();


}
