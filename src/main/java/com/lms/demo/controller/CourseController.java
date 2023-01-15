package com.lms.demo.controller;

import com.lms.demo.dto.request.CourseRequestDto;

import com.lms.demo.dto.response.CourseResponseDto;
import com.lms.demo.exception.CourseNotFoundException;
import com.lms.demo.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1.0/lms/courses")
@Tag(name = "Courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private static final String COURSE_NOT_FOUND = "Course Not Found";

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    @GetMapping(value = "/getAll")
    @Operation(summary = "GET /getAll", description = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course Data Retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "Course Not Found" , content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error" , content = {@Content()})
    })
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {

        List<CourseResponseDto> courses = courseService.getCourses();
        if(courses.isEmpty()) {
            LOG.error("Course data not found");
            throw new CourseNotFoundException(COURSE_NOT_FOUND);
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value = "/info/{technology}")
    @Operation(summary = "GET /info/{technology}", description = "Get courses based on given technology")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course Data Retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "Course Not Found", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content()})
    })
    public ResponseEntity<List<CourseResponseDto>> getCoursesBasedOnTechnology(@PathVariable("technology") String technology){

        List<CourseResponseDto> courses = courseService.getCourses(technology);
        if(courses.isEmpty()) {
            LOG.error("Course data not found for {}", technology);
            throw new CourseNotFoundException(COURSE_NOT_FOUND);
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @GetMapping(value = "/get/{technology}/{durationFromRange}/{durationToRange}")
    @Operation(summary = "GET /get/{technology}/{durationFromRange}/{durationToRange}",
            description = "Get courses based on given technology and duration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course Data Retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "Course Not Found", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content()})
    })
    public ResponseEntity<List<CourseResponseDto>> getCoursesBasedOnTechAndDuration(@PathVariable("technology") String technology,
                                                 @PathVariable("durationFromRange") int durationFromRange,
                                                 @PathVariable("durationToRange") int durationToRange){

        List<CourseResponseDto> courses = courseService.getCourses(technology, durationFromRange, durationToRange);
        if(courses.isEmpty()) {
            LOG.error("Course data not found for {} technology and duration between {} and {}", technology, durationFromRange, durationToRange);
            throw new CourseNotFoundException(COURSE_NOT_FOUND);
        }

        return new ResponseEntity<>(courses, HttpStatus.OK);

    }

    @PostMapping(value= "/add")
    @Operation(summary = "POST /add", description = "Add Course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course added", content = {@Content()}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content()}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content()})
    })
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> addCourse(@Valid @RequestBody CourseRequestDto course) {
        courseService.addCourse(course);
        return new ResponseEntity<>("Course added", HttpStatus.CREATED);

    }

    @DeleteMapping(value = "/delete/{courseId}")
    @Operation(summary = "DELETE /delete/{courseId}", description = "delete course based on given Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted", content = {@Content()}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "Course Not Found", content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content()})
    })
    public ResponseEntity<String> deleteCourse(@PathVariable("courseId") String courseId){
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);

    }



}
