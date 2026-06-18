package com.recordedapp.recordedback.dtos;

import jakarta.validation.constraints.NotBlank;

/*FoodSearchDTO class implemented 4/24/2026 in order to search for the food in the USDA food database
*
 */
public class FoodSearchRequestDTO {
    @NotBlank
    private String foodName;

    @NotBlank
    private double foodQuantity;

    //constructor
    public FoodSearchRequestDTO(String foodName, double foodQuantity){
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
    }

    //getter and setter methods
    public String getFoodName(){
        return foodName;
    }
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public double getFoodQuantity(){
        return foodQuantity;
    }

    public void setFoodQuantity(double foodQuantity){
        this.foodQuantity = foodQuantity;
    }
}
