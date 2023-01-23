package com.lms.demo.service;

import com.lms.demo.dto.request.CourseRequestDto;
import com.lms.demo.dto.response.CourseResponseDto;
import com.lms.demo.exception.CourseNotFoundException;
import com.lms.demo.model.Course;
import com.lms.demo.repository.CourseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseRepository courseRepository;

    private static final CourseRequestDto courseRequestDto = CourseRequestDto.buildCourseRequestDtoWith()
            .name("The Angular Framework")
            .technology("Web Framework")
            .description("The Complete Angular Framework")
            .launchUrl("www.angular.io")
            .duration(1200)
            .build();

    private static final Course course1 = Course.buildCourseWith()
            .id("A1234")
            .name("The Angular Framework")
            .technology("Web Framework")
            .description("The Complete Angular Framework")
            .launchUrl("www.angular.io")
            .duration(1200)
            .build();

    private static final Course course2 = Course.buildCourseWith()
            .id("A1235")
            .name("The React Framework")
            .technology("Web Framework")
            .description("The Complete React Framework Guide")
            .launchUrl("www.angular.io")
            .duration(1400)
            .build();

    private static List<Course> courses;

    private static final List<Course> emptyCourseList = Collections.emptyList();

    @BeforeAll
    public static void initialiseVariables(){
        courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
    }


    @Test
    void givenUnavailableTechnologyThenReturnEmptyList(){
        when(courseRepository.findItemsByTechnology(anyString())).thenReturn(emptyCourseList);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses(anyString());
        assertTrue(courseResponseDtoList.isEmpty());
    }

    @Test
    void givenCourseTechnologyGetFilteredCourses(){
        when(courseRepository.findItemsByTechnology(anyString())).thenReturn(courses);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses(anyString());
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void givenUnavailableTechnologyAndDurationThenReturnEmptyList(){
        when(courseRepository.findItemsByTechnologyAndDuration(anyString(),anyInt(), anyInt())).thenReturn(emptyCourseList);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses(anyString(), anyInt(), anyInt());
        assertTrue(courseResponseDtoList.isEmpty());
    }

    @Test
    void givenCourseTechnologyAndDurationGetFilteredCourses(){
        when(courseRepository.findItemsByTechnologyAndDuration(anyString(),anyInt(), anyInt())).thenReturn(courses);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses(anyString(), anyInt(), anyInt());
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void givenEmptyDatabaseThenReturnEmptyList(){
        when(courseRepository.findAll()).thenReturn(emptyCourseList);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses();
        assertTrue(courseResponseDtoList.isEmpty());
    }

    @Test
    void getAllCourses(){
        when(courseRepository.findAll()).thenReturn(courses);
        List<CourseResponseDto> courseResponseDtoList = courseService.getCourses();
        assertEquals(2, courseResponseDtoList.size());
    }

    @Test
    void givenUnavailableCourseIdThenReturnCourseNotFoundException(){
        String anyString = anyString();
        when(courseRepository.existsById(anyString)).thenReturn(false);
        Exception exception = assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(anyString));
        assertEquals("Course doesn't exists", exception.getMessage());
    }

    @Test
    void givenAvailableCourseIdThenDeleteCourse(){
        when(courseRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(courseRepository).deleteById(anyString());
        courseService.deleteCourse(anyString());
        verify(courseRepository).deleteById(anyString());
    }

    @Test
    void givenCourseRequestDtoSaveCourse(){
       when(courseRepository.save(Mockito.any())).thenReturn(Mockito.any());
        courseService.addCourse(courseRequestDto);
        verify(courseRepository).save(Mockito.any());
    }
}
