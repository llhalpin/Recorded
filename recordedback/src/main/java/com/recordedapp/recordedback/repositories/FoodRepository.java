/**
 * FoodRepository.java is an interface that extends JpaRepository which provides the CRUD methods(Create, Read, Update,
 * and Delete) for the "food_table" table in the database
 * Created by llhalpin 8-19-2025
 */
package com.recordedapp.recordedback.repositories;

import com.recordedapp.recordedback.models.Food;
import com.recordedapp.recordedback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByUser(User user);
}