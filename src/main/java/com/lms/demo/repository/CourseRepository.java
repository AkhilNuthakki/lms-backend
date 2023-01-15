package com.lms.demo.repository;

import com.lms.demo.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {

    @Query(value = "{technology:'?0'}")
    List<Course> findItemsByTechnology(String technology);


    @Query(value = "{$and:[{technology:'?0'},{duration:{$gte:?1}},{duration:{$lte:?2}}]}")
    List<Course> findItemsByTechnologyAndDuration(String technology, int durationFromRange, int durationToRange);

    List<Course> findAll();


}
