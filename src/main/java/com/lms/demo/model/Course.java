package com.lms.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("courses")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "buildCourseWith")
public class Course {

    @Id
    private String id;

    private String name;

    private int duration;

    private String description;

    private String technology;

    private String launchUrl;

}
