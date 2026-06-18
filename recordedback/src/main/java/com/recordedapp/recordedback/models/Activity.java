package com.recordedapp.recordedback.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="activities_table")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityName;
    private LocalDate date;
    private Integer duration;
    private Integer calories;
    private String notes;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable=false )
    private User user; //llh 04/11/2025 add User

    //default constructor (required by JPA)
    public Activity(){}

    //Parameterized constructor
    public Activity(Long id, String activityName, LocalDate date, Integer duration, Integer calories, String notes, User user) {
        this.id =id;
        this.activityName = activityName;
        this.date = date;
        this.duration = duration; //in minutes
        this.calories = calories;
        this.notes = notes;
        this.user = user;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getActivityName(){
        return activityName;
    }
    public void setActivityName(String activityName){
        this.activityName = activityName;
    }

    public LocalDate getDate(){
        return date;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }

    public Integer getDuration(){
        return duration;
    }
    public void setDuration(Integer duration){
        this.duration = duration;
    }

    public Integer getCalories(){
        return calories;
    }
    public void setCalories(Integer calories){
        this.calories = calories;
    }

    public String getNotes(){
        return notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }

    //llh 04/11/2025 add getUser and setUser methods to access User
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Activity{"+
                "id=" +id +
                ", activity='"+activityName + '\'' +
                ", date="+ date +
                ", duration="+ duration +
                ", calories="+ calories +
                ", notes='"+ notes +'\'' +
                '}';
    }

}
