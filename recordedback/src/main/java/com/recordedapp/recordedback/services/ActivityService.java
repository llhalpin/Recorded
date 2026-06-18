package com.recordedapp.recordedback.services;

import com.recordedapp.recordedback.models.Activity;
import com.recordedapp.recordedback.models.User;
import com.recordedapp.recordedback.repositories.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/* ActivityService class provides the methods to interact with the database
:llh 7-23-2025 - added @Transactional annotation to create, update and delete methods to safeguard database
so that no changes will be made to the database if there is an error
 */
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    //get a list of all activities
    public List<Activity> getAllActivities(){
        return activityRepository.findAll();
    }

    public Optional<Activity> getActivityById(Long id){
        return activityRepository.findById(id);
    }

    //llh 04/11/2025 get user's list of activities
    public List<Activity> getActivitiesByUser(User user){
        return activityRepository.findByUser(user);
    }


    //llh 04/12/2025 create a new activity and saves it and now links it to the user
    @Transactional //adding this annotation to safeguard database so that no changes will be made to the database if there is an error :llh 7-24-2025
    public Activity addActivity(Activity activity, User user){
        activity.setUser(user); // llh 4/11/2025: link user to activity created
        return activityRepository.save(activity);
    }


    //update existing activity with specified id
    @Transactional //adding this annotation to safeguard database so that no changes will be made to the database if there is an error :llh 7-24-2025
    public Activity updateActivity(Long id, Activity activityDetails){
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        if (optionalActivity.isPresent()) {
            Activity activity =optionalActivity.get();
            activity.setActivityName(activityDetails.getActivityName());
            activity.setDate(activityDetails.getDate());
            activity.setDuration(activityDetails.getDuration());
            activity.setCalories(activityDetails.getCalories());
            activity.setNotes(activityDetails.getNotes());
            System.out.println("ActivityService.java: updateActivity activity="+activity);//llh 8-13-2025

            return activityRepository.save(activity);
        } else {
            throw new RuntimeException("Activity not found with id: " + id);
        }
    }

    //Delete an activity with specified id
    @Transactional
    public void deleteActivityById(Long id){
        if (activityRepository.existsById(id)){
            activityRepository.deleteById(id);
        } else {
            throw new RuntimeException("Activity not found with id: " + id);
        }
    }

}
