package com.example.restaurant;

import java.util.ArrayList;
import java.util.List;

/* A "wrapper" around the MySQL database that supports insertion and deletion (based on index) */
public class RestaurantDB {
    public List<Dish> getDishesByCategory(Category category)
    {
        return null;
    }

    public void setDish(int did)
    {

    }

    public void deleteDish(int did)
    {

    }


    public enum Category {
        appetizer,
        lunch,
        dinner,
        drink
    }
}
