package ru.dan.content.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Payload for creating a new tag")
data class TagCreateDto(
    @Schema(description = "Name of the new tag", example = "spring-boot", required = true)
    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    val name: String
)