package com.recordedapp.recordedback.repositories;

import com.recordedapp.recordedback.models.Activity;
import com.recordedapp.recordedback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//This interface provides the CRUD operations(Create,Read,Update and Delete) for the "activities_table" table in database
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    //llh 4_13_2025 changed to findByUser(User user) from findByUser() since Spring was throwing an error in not finding User
    List<Activity> findByUser(User user);
}
