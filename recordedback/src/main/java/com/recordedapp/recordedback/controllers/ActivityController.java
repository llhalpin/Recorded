
package com.recordedapp.recordedback.controllers;

import com.recordedapp.recordedback.models.Activity;
import com.recordedapp.recordedback.models.User;
import com.recordedapp.recordedback.services.ActivityService;
import com.recordedapp.recordedback.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequestMapping("/api/activity") //activity webpage
@CrossOrigin(origins = "http://localhost:5173") //local host for webpage
/* llhalpin: created February 2025
ActivityController class - provides methods to process HTTP request for activity, such as exercise, meditation, cleaning,
dancing and etc.,  tracking webpage
7-23-2025: added annotation PreAuthorize to check username matches URL path username from frontend matches with backend and
added annotation(EnableMethodSecurity) in SecurityConfig class in order to use PreAuthorize
 */
public class ActivityController {

    private final ActivityService activityService;
    private final UserService userService;

    //constructor
    public ActivityController(ActivityService activityService, UserService userService){
        this.activityService = activityService;
        this.userService = userService;
    }

    //GET all activities for every user (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')") //llh 7-23-2025 added this for admin to see all activities of every user
    @GetMapping
    public List<Activity> getAllActivities(){
        return activityService.getAllActivities();
    }

    //GET ActivitiesByUser method to retrieve activities to a specific user :llh 04-12-2025
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')") //llh 7-23-2025
    @GetMapping("/user/{username}")
    public List<Activity> getActivitiesByUser(@PathVariable String username){
        return userService.findByUsername(username)
                .map(activityService::getActivitiesByUser)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    //GET one activity by ID for a specific user
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    @GetMapping("/user/{username}/{id}")
    public Activity getActivityById(@PathVariable String username, @PathVariable Long id){
        //get user
        User user = userService.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found: " + username));

        //get activity based on id
        Activity activity = activityService.getActivityById(id)
                .orElseThrow(()-> new RuntimeException("Activity not found by id: "+id));

        //verify activity belongs to user
        if(!activity.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Activity does not belong to user: "+ username);
        }

        return activity;
    }

    //POST - create new activity for user :llh 04-12-2025 creating an activity will be linked to user going forward in this current change
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')") //llh 7-23-2025 added this annotation
    @PostMapping("/user/{username}")
    public Activity addActivity(@Valid @RequestBody Activity activity, @PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found: "+ username));
        return activityService.addActivity(activity, user);
    }

    //PUT - update activity for user
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    @PutMapping("/user/{username}/{id}")
    public Activity updateActivity(@PathVariable String username, @PathVariable Long id, @RequestBody Activity activity){
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: "+ username));
        Activity existingActivity = activityService.getActivityById(id).orElseThrow(() -> new RuntimeException("Activity not found: "+ id));

        if (!existingActivity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Activity does not belong to user: " +username);
        }

        activity.setId(id);
        activity.setUser(user);
        System.out.println("ActivityController.java : updateActivity: id="+id+" user="+user); //lh 8-13-2025
        return activityService.updateActivity(id, activity);
    }

    //DELETE -Delete activity by id for user
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    @DeleteMapping("/user/{username}/{id}")
    public void deleteActivityById(@PathVariable String username, @PathVariable Long id){
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: "+ username));
        Activity activity = activityService.getActivityById(id).orElseThrow(() -> new RuntimeException("Activity not found: "+id));

        if (!activity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Activity does not belong to user: " + username);
        }
        activityService.deleteActivityById(id);
    }



}
