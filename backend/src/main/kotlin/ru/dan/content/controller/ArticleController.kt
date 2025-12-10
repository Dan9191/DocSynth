package ru.dan.content.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.dan.content.model.ArticleViewDto
import ru.dan.content.model.ShortArticleDto
import ru.dan.content.service.ArticleService

@RestController
@RequestMapping("/api/v1/articles")
@Tag(name = "articles", description = "Operations related to articles")
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping
    fun getAllTags(): Flux<ShortArticleDto> {
        return articleService.getAllShortArticles()
    }

    @Operation(summary = "Get article by ID", description = "Get a specific article by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Article found",
                content = [Content(schema = Schema(implementation = ArticleViewDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Article not found",
                content = [Content()]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getArticle(@PathVariable id: Long): Mono<ArticleViewDto> {
        return articleService.getArticleById(id)
    }

    /**
     * Тестовый метод.
     */
    @GetMapping("/hi")
    fun hi(): Mono<String> {
        return Mono.just("Hello from hotel service")
    }
}
