package ru.dan.content.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

/**
 * Tag.
 */
@Table("tag")
class Tag(
    @Id
    val id: Long? = null,
    val name: String
)
