package ru.dan.content.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Tag data transfer object")
data class TagDto(
    @Schema(description = "Unique identifier of the tag", example = "1")
    val id: Long? = null,

    @Schema(description = "Name of the tag", example = "kotlin")
    val name: String
)
