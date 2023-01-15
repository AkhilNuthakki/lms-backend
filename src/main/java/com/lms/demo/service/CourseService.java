package com.lms.demo.service;

import com.lms.demo.dto.request.CourseRequestDto;
import com.lms.demo.dto.response.CourseResponseDto;

import java.util.List;

public interface CourseService {

    public void addCourse(CourseRequestDto courseRequestDto);

    public List<CourseResponseDto> getCourses();

    public List<CourseResponseDto> getCourses(String technology);

    public List<CourseResponseDto> getCourses(String technology, int durationFromRange, int durationToRange);

    public void deleteCourse(String courseId);
}
