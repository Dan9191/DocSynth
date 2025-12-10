package ru.dan.content.model

import io.swagger.v3.oas.annotations.media.Schema
import ru.dan.content.model.TagDto

/**
 * DTO для представления секции.
 */
@Schema(description = "DTO для представления секции")
data class SectionViewDto(

    @Schema(description = "Уникальный идентификатор секции")
    val id: Long? = null,

    @Schema(description = "Название секции")
    val name: String,

    @Schema(description = "Описание секции")
    val description: String? = null,

    @Schema(description = "Id родительской секции")
    val parentId: Long? = null,

    @Schema(description = "Список тегов, связанных с секцией")
    val tags: List<TagDto> = emptyList()
)
