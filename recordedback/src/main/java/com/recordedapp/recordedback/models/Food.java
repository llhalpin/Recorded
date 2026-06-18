package com.recordedapp.recordedback.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="food_table")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private LocalDate date;
    private Integer quantity;
    private Integer calories;
    private Integer protein;
    private Integer fiber;
    private Integer fat;
    private Integer carb;
    private String notes;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Food(){}

    public Food(Long id, String foodName, LocalDate date, Integer quantity, Integer calories,
                Integer protein, Integer fiber, Integer fat, Integer carb, String notes, User user){
        this.id = id;
        this.foodName = foodName;
        this.date = date;
        this.quantity = quantity;
        this.calories = calories;
        this.protein = protein;
        this.fiber = fiber;
        this.fat = fat;
        this.carb = carb;
        this.notes = notes;
        this.user = user;

    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getFoodName(){
        return foodName;
    }

    public void setFoodName(String foodName){
        this.foodName = foodName;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    @Override
    public String toString(){
        return "Food{"+
                "id="+id +
                ", food="+ foodName +
                ", date="+ date +
                ", quantity="+ quantity +
                ", calories="+ calories +
                ", protein="+ protein +
                ", fiber="+fiber +
                ", fat="+ fat +
                ", carb="+carb +
                ", notes='"+ notes +'\'' +
                '}';
    }



}
