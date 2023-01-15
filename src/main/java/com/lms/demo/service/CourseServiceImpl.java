package com.lms.demo.service;

import com.lms.demo.dto.request.CourseRequestDto;
import com.lms.demo.dto.response.CourseResponseDto;
import com.lms.demo.exception.CourseNotFoundException;
import com.lms.demo.model.Course;
import com.lms.demo.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);

    public void addCourse(CourseRequestDto courseRequestDto) {
        LOG.info("Building course object from the json request");
        Course course = Course.buildCourseWith()
                .name(courseRequestDto.getName())
                .description(courseRequestDto.getDescription())
                .technology(courseRequestDto.getTechnology())
                .duration(courseRequestDto.getDuration())
                .launchUrl(courseRequestDto.getLaunchUrl())
                .build();
        courseRepository.save(course);

    }

    public List<CourseResponseDto> getCourses() {
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();

        LOG.info("Fetching courses information from database");

        courseRepository.findAll().forEach(course -> {
            CourseResponseDto courseResponseDto = CourseResponseDto.buildCourseDetailsWith()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .duration(course.getDuration())
                    .technology(course.getTechnology())
                    .launchUrl(course.getLaunchUrl())
                    .build();
            courseResponseDtoList.add(courseResponseDto);
        });

        return courseResponseDtoList;
    }

    public List<CourseResponseDto> getCourses(String technology) {
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();

        LOG.info("Fetching courses information for {} technology from database", technology);

        courseRepository.findItemsByTechnology(technology).forEach(course -> {
            CourseResponseDto courseResponseDto = CourseResponseDto.buildCourseDetailsWith()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .duration(course.getDuration())
                    .technology(course.getTechnology())
                    .launchUrl(course.getLaunchUrl())
                    .build();
            courseResponseDtoList.add(courseResponseDto);
        });
        return courseResponseDtoList;
    }

    public List<CourseResponseDto> getCourses(String technology, int durationFromRange, int durationToRange) {
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();

        LOG.info("Fetching courses information for {} technology and for duration between {} and {} from database", technology, durationFromRange, durationToRange);

        courseRepository.findItemsByTechnologyAndDuration(technology, durationFromRange, durationToRange).forEach(course -> {
            CourseResponseDto courseResponseDto = CourseResponseDto.buildCourseDetailsWith()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .duration(course.getDuration())
                    .technology(course.getTechnology())
                    .launchUrl(course.getLaunchUrl())
                    .build();

            courseResponseDtoList.add(courseResponseDto);
        });

        return courseResponseDtoList;
    }

    public void deleteCourse(String courseId) throws CourseNotFoundException{
        LOG.info("Checking if the course exists by the courseId");
        if(courseRepository.existsById(courseId)){
            LOG.info("Course exists by given courseId");
            courseRepository.deleteById(courseId);
        } else {
            throw new CourseNotFoundException("Course doesn't exists");
        }
    }

}
