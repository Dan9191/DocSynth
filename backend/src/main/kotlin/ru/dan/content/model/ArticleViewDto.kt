package ru.dan.content.model

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * DTO для представления статьи.
 */
@Schema(description = "DTO для представления статьи")
data class ArticleViewDto(
    @Schema(description = "Уникальный идентификатор статьи")
    val id: Long,

    @Schema(description = "Заголовок статьи")
    val title: String,

    @Schema(description = "Описание статьи")
    val description: String,

    @Schema(description = "Ссылка на основное изображение статьи")
    val mainPicture: String,

    @Schema(description = "Источник статьи")
    val source: String,

    @Schema(description = "Содержимое статьи в формате JSON")
    val body: JsonNode,

    @Schema(description = "Дата создания статьи")
    val createdAt: Instant,

    @Schema(description = "Дата последнего обновления статьи")
    val updatedAt: Instant?,

    @Schema(description = "Количество просмотров статьи")
    val viewCount: Long,

    @Schema(description = "Идентификатор раздела, к которому относится статья")
    val sectionId: Long,

    @Schema(description = "Список тегов, связанных со статьей")
    val tags: List<TagDto> = emptyList()
)
