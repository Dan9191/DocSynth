package ru.dan.content.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import ru.dan.content.entity.Article
import java.time.Instant

interface ArticleRepository : ReactiveCrudRepository<Article, Long> {
    interface ArticleShortProjection {
        val id: Long?
        val mainPicture: String
        val title: String
        val description: String
        val sectionId: Long
        val createdAt: Instant
    }

    @Query("SELECT id, main_picture, title, description, section_id, created_at FROM article")
    fun findAllShort(): Flux<ArticleShortProjection>

}

