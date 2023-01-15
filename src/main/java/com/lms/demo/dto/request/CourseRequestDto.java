package com.lms.demo.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.*;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "buildCourseRequestDtoWith")
public class CourseRequestDto {
    @NotBlank(message = "Course name is required")
    @Size(min = 20, message = "Course name should be minimum of 20 characters")
    private String name;

    @NotNull(message = "Course duration is required")
    @Min(value = 1, message = "The duration should be more than zero")
    private int duration;

    @NotBlank(message = "Course description is required")
    @Size(min = 100, message = "Course description should be minimum of 100 characters")
    private String description;

    @NotBlank(message = "Course technology is required")
    private String technology;

    @NotBlank(message = "Course launch url is required")
    private String launchUrl;
}
