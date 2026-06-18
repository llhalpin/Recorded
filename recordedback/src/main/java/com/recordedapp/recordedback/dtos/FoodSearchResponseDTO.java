package com.recordedapp.recordedback.dtos;

import jakarta.validation.constraints.NotBlank;

/*FoodNutritionResponseDTO created 4/23/2026 (LLHalpin) for the implementation of tracking food nutrition
* Note that using food prefix to differentiate from activities' calories burned and consistency for
* the rest of the variables
*/
public class FoodSearchResponseDTO {

    private String foodUsdaId; //unique food ID from USDA

    @NotBlank
    private String foodName;
    
    @NotBlank
    private double foodQuantityInGrams;
    
    private double foodCalories;
    
    private double foodProtein;
    
    private double foodFat;
    
    private double foodCarb;
    
    private double foodFiber;

    //constructor
    public FoodSearchResponseDTO(String foodUsdaId, String foodName, double foodQuantityInGrams, double foodCalories,
                                 double foodProtein, double foodFat, double foodCarb, double foodFiber){
        this.foodUsdaId = foodUsdaId;
        this.foodName = foodName;
        this.foodQuantityInGrams = foodQuantityInGrams;
        this.foodCalories = foodCalories;
        this.foodProtein = foodProtein;
        this.foodFat = foodFat;
        this.foodCarb = foodCarb;
        this.foodFiber = foodFiber;
    }

    //getter and setter methods
    public String getFoodUsdaId(){
        return foodUsdaId;
    }

    public void setFoodUsdaId(String foodUsdaId){
        this.foodUsdaId = foodUsdaId;
    }

    public String getFoodName(){
        return foodName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public double getFoodQuantityInGrams(){
        return foodQuantityInGrams;
    }

    public void setFoodQuantityInGrams(double foodQuantityInGrams){
        this.foodQuantityInGrams = foodQuantityInGrams;
    }

    public double getFoodCalories(){
        return foodCalories;
    }

    public void setFoodCalories(double foodCalories){
        this.foodCalories = foodCalories;
    }

    public double getFoodProtein(){
        return foodProtein;
    }

    public void setFoodProtein(double foodProtein){
        this.foodProtein = foodProtein;
    }

    public double getFoodFat(){
        return foodFat;
    }

    public void setFoodFat(double foodFat){
        this.foodFat = foodFat;
    }

    public double getFoodCarb(){
        return foodCarb;
    }

    public void setFoodCarb(double foodCarb){
        this.foodCarb = foodCarb;
    }

    public double getFoodFiber(){
        return foodFiber;
    }

    public void setFoodFiber(double foodFiber){
        this.foodFiber = foodFiber;
    }

}
