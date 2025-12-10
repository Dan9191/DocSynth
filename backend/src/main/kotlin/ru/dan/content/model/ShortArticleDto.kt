package ru.dan.content.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

class ShortArticleDto (

    val id: Long? = null,
    val mainPicture: String,
    val title: String,
    val description: String,
    val tags: List<TagDto>,
    @Schema(description = "Идентификатор раздела, к которому относится статья")
    val sectionId: Long,
    val createdAt: Instant,
)