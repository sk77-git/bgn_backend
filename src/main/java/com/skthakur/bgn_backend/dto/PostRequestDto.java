package com.skthakur.bgn_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skthakur.bgn_backend.enums.ResourceType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class PostRequestDto {
    @JsonProperty("id")
    Long id;
    @JsonProperty("file")
    @NotNull(message = "File is required")
    private MultipartFile file;

    @JsonProperty("title")
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    @JsonProperty("description")
    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("author")
    @NotBlank(message = "Author is required")
    private String author;

    @JsonProperty("author_info")
    @NotBlank(message = "Author information is required")
    private String authorInfo;

    @JsonProperty("resource_type")
    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    @JsonProperty("semester")
    @Min(message = "Semester cannot be less than 1", value = 1)
    @Max(message = "Semester cannot be more than 8", value = 8)
    @NotNull(message = "Semester is required")
    private Integer semester;
}
