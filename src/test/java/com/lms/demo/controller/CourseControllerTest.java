package com.lms.demo.controller;

import com.lms.demo.dto.response.CourseResponseDto;
import com.lms.demo.exception.CourseNotFoundException;
import com.lms.demo.service.CourseServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseServiceImpl courseService;

    private static final CourseResponseDto course1 = CourseResponseDto.buildCourseDetailsWith()
            .id("A1234")
            .name("The Angular Framework")
            .technology("Web Framework")
            .description("The Complete Angular Framework")
            .launchUrl("www.angular.io")
            .duration(1200)
            .build();

    private static final CourseResponseDto course2 = CourseResponseDto.buildCourseDetailsWith()
            .id("A1235")
            .name("The React Framework")
            .technology("Web Framework")
            .description("The Complete React Framework Guide")
            .launchUrl("www.angular.io")
            .duration(1400)
            .build();

    private static List<CourseResponseDto> courseResponseDtoList;

    private static final List<CourseResponseDto> emptycourseResponseDtoList = Collections.emptyList();

    @BeforeAll
    public static void initialiseVariables(){
        courseResponseDtoList = new ArrayList<>();
        courseResponseDtoList.add(course1);
        courseResponseDtoList.add(course2);
    }

    @Test
    void givenUnavailableTechnologyThenCourseNotFoundException(){
        String anyString = anyString();
        when(courseService.getCourses(anyString)).thenReturn(emptycourseResponseDtoList);
        Exception exception = assertThrows(CourseNotFoundException.class,
                () -> courseController.getCoursesBasedOnTechnology(anyString));
        assertEquals("Course Not Found", exception.getMessage());
    }

    @Test
    void givenCourseTechnologyGetFilteredCourses(){
        when(courseService.getCourses(anyString())).thenReturn(courseResponseDtoList);
        ResponseEntity<List<CourseResponseDto>> response = courseController.getCoursesBasedOnTechnology(anyString());
        List<CourseResponseDto> courseResponseDtoList = response.getBody();
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void givenUnavailableTechnologyAndDurationThenCourseNotFoundException(){
        String anyString = anyString();
        int anyInt = anyInt();
        int anyInt2 = anyInt();
        when(courseService.getCourses(anyString, anyInt, anyInt2)).thenReturn(emptycourseResponseDtoList);
        Exception exception = assertThrows(CourseNotFoundException.class,
                () -> courseController.getCoursesBasedOnTechAndDuration(anyString, anyInt, anyInt2));
        assertEquals("Course Not Found", exception.getMessage());
    }

    @Test
    void givenCourseTechnologyAndDurationGetFilteredCourses(){
        when(courseService.getCourses(anyString(),anyInt(), anyInt())).thenReturn(courseResponseDtoList);
        ResponseEntity<List<CourseResponseDto>> response = courseController.getCoursesBasedOnTechAndDuration
                (anyString(), anyInt(), anyInt());
        List<CourseResponseDto> courseResponseDtoList = response.getBody();
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void givenNoCourseDataAvailableThenCourseNotFoundException(){
        when(courseService.getCourses()).thenReturn(emptycourseResponseDtoList);
        Exception exception = assertThrows(CourseNotFoundException.class,
                () -> courseController.getAllCourses());
        assertEquals("Course Not Found", exception.getMessage());
    }

    @Test
    void givenNoCourseTechnologyAndDurationGetAllCourses(){
        when(courseService.getCourses()).thenReturn(courseResponseDtoList);
        ResponseEntity<List<CourseResponseDto>> response = courseController.getAllCourses();
        List<CourseResponseDto> courseResponseDtoList = response.getBody();
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void courseAddedWhenProvidedValidCourseDetails(){
        doNothing().when(courseService).addCourse(any());
        ResponseEntity<String> response = courseController.addCourse(any());
        assertEquals("Course added", response.getBody());
    }

    @Test
    void courseDeleteWhenProvidedCourseId(){
        doNothing().when(courseService).deleteCourse(anyString());
        ResponseEntity<String> response = courseController.deleteCourse(anyString());
        assertEquals("Course deleted", response.getBody());
    }

}
