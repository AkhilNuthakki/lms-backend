package com.lms.demo.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(builderMethodName = "buildCourseDetailsWith")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseResponseDto {

    private String id;

    private String name;

    private int duration;

    private String description;

    private String technology;

    private String launchUrl;
}
