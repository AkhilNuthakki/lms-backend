package com.lms.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.demo.config.JWTAuthTokenConfiguration;
import com.lms.demo.controller.CourseController;
import com.lms.demo.dto.request.CourseRequestDto;
import com.lms.demo.dto.response.CourseResponseDto;
import com.lms.demo.service.CourseServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LmsBackendApplicationTests {


	@Mock
	private CourseController courseController;

	@MockBean
	private CourseServiceImpl courseService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JWTAuthTokenConfiguration jwtAuthTokenConfiguration;

	private String TOKEN = null;

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

	private static final CourseRequestDto courseRequestDto = CourseRequestDto.buildCourseRequestDtoWith()
			.name("The Complete Angular Framework Guide")
			.technology("Web Framework")
			.description("The Complete Angular Framework Guide by arnold gives the better" +
					"understanding of the frontend framework")
			.launchUrl("www.angular.io")
			.duration(1200).build();

	private static List<CourseResponseDto> courseResponseDtoList;

	private static final List<CourseResponseDto> emptycourseResponseDtoList = Collections.emptyList();

	@BeforeAll
	public void initialiseVariables(){
		courseResponseDtoList = new ArrayList<>();
		courseResponseDtoList.add(course1);
		courseResponseDtoList.add(course2);
		SignedJWT signedJWT = jwtAuthTokenConfiguration.generateToken("ADMIN");
		TOKEN = signedJWT.serialize();
	}


	@Test
	void givenUnavailableTechnologyThenCourseNotFoundExceptionRestAction() throws Exception {
		when(courseService.getCourses(anyString())).thenReturn(emptycourseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/lms/courses/info/{technology}", "Web")
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	void givenCourseTechnologyGetFilteredCoursesRestAction() throws Exception {
		when(courseService.getCourses(anyString())).thenReturn(courseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/lms/courses/info/{technology}", "Web")
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void givenUnavailableTechnologyAndDurationThenCourseNotFoundExceptionRestAction() throws Exception {
		when(courseService.getCourses(anyString(), anyInt(), anyInt())).thenReturn(emptycourseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.
				get("/api/v1.0/lms/courses/get/{technology}/{durationFromRange}/{durationToRange}",
						"Web", 1200, 1400)
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	void givenCourseTechnologyAndDurationGetFilteredCoursesRestAction() throws Exception {
		when(courseService.getCourses(anyString(),anyInt(), anyInt())).thenReturn(courseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.
				get("/api/v1.0/lms/courses/get/{technology}/{durationFromRange}/{durationToRange}",
						"Web", 1200, 1400)
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void givenNoCourseDataAvailableThenCourseNotFoundExceptionRestAction() throws Exception {
		when(courseService.getCourses()).thenReturn(emptycourseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/lms/courses/getAll")
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	void givenNoCourseTechnologyAndDurationGetAllCoursesRestAction() throws Exception {
		when(courseService.getCourses()).thenReturn(courseResponseDtoList);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/lms/courses/getAll")
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void courseAddedWhenProvidedValidCourseDetailsRestAction() throws Exception {
		doNothing().when(courseService).addCourse(any());
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/lms/courses/add")
				.header("Authorization", "Bearer " + TOKEN)
				.content(asJsonString(courseRequestDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	private static String asJsonString(final CourseRequestDto obj) {
		try{
			return new ObjectMapper().writeValueAsString((obj));
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Test
	void courseDeleteWhenProvidedCourseIdRestAction() throws Exception {
		doNothing().when(courseService).deleteCourse(anyString());
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1.0/lms/courses/delete/{courseId}", "Course1234")
				.header("Authorization", "Bearer " + TOKEN)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}


}
