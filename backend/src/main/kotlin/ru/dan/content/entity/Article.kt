package ru.dan.content.entity

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

/**
 * Сущность для статьи.
 */
@Table("article")
class Article(
    @Id
    val id: Long? = null,
    val title: String,
    val description: String,
    @Column("main_picture")
    val mainPicture: String,
    val source: String,
    val body: Json,
    @Column("created_at")
    val createdAt: Instant = Instant.now(),
    @Column("updated_at")
    val updatedAt: Instant?,
    @Column("view_count")
    val viewCount: Long = 0,
    @Column("section_id")
    val sectionId: Long
)
