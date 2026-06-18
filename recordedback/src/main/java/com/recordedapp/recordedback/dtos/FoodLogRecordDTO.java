package com.recordedapp.recordedback.dtos;

/* FoodLogRecordDTO class implemented 4/24/2026 to record the food that the user want to track
*
*/
public class FoodLogRecordDTO {

    private String foodUsdaId;
    private String foodName;
    private double foodQuantityInGrams;
    private double foodCalories;
    private double foodProtein;
    private double foodCarb;
    private double foodFat;
    private double foodFiber;

    //constructor
    public FoodLogRecordDTO(String foodUsdaId, String foodName, double foodQuantityInGrams,
                            double foodCalories, double foodProtein, double foodCarb, double foodFat,
                            double foodFiber){
        this.foodUsdaId = foodUsdaId;
        this.foodName = foodName;
        this.foodQuantityInGrams = foodQuantityInGrams;
        this.foodCalories = foodCalories;
        this.foodProtein = foodProtein;
        this.foodCarb = foodCarb;
        this.foodFat = foodFat;
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

    public double getFoodCarb(){
        return foodCarb;
    }

    public void setFoodCarb(double foodCarb){
        this.foodCarb = foodCarb;
    }

    public double getFoodFat(){
        return foodFat;
    }

    public void setFoodFat(double foodFat){
        this.foodFat = foodFat;
    }

    public double getFoodFiber(){
        return foodFiber;
    }

    public void setFoodFiber(double foodFiber){
        this.foodFiber = foodFiber;
    }
}
