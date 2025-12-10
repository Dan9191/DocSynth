package ru.dan.content.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.dan.content.model.ArticleViewDto
import ru.dan.content.model.ShortArticleDto
import ru.dan.content.model.TagDto
import ru.dan.content.repository.ArticleRepository
import ru.dan.content.repository.TagRepository

private val logger = KotlinLogging.logger {}

/**
 * Сервис работы со статьями
 */
@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository,
    private val objectMapper: ObjectMapper
) {

    /**
     * Поиск статьи по id.
     * @param id идентификатор статьи
     * @return Mono<Article> — либо статья, либо пустой Mono, если не найдено
     */
    @Transactional
    fun getArticleById(id: Long): Mono<ArticleViewDto> {
        logger.info { "Fetching article by id $id" }
        return articleRepository.findById(id)
            .flatMap { article ->
                tagRepository.findAllByArticleId(article.id!!)
                    .map { tag -> TagDto(tag.id, tag.name) }
                    .collectList()
                    .map { tags ->
                        ArticleViewDto(
                            id = article.id,
                            title = article.title,
                            description = article.description,
                            mainPicture = article.mainPicture,
                            source = article.source,
                            body = objectMapper.readValue(article.body.asString(), JsonNode::class.java),
                            createdAt = article.createdAt,
                            updatedAt = article.updatedAt,
                            viewCount = article.viewCount,
                            sectionId = article.sectionId,
                            tags = tags
                        )
                    }
            }
    }

    @Transactional
    fun getAllShortArticles(): Flux<ShortArticleDto> {
        return articleRepository.findAllShort()
            .flatMap { article ->
                tagRepository.findAllByArticleId(article.id!!)
                    .map { tag -> TagDto(tag.id, tag.name) }
                    .collectList()
                    .map { tags ->
                        ShortArticleDto(
                            id = article.id,
                            mainPicture = article.mainPicture,
                            title = article.title,
                            description = article.description,
                            tags = tags,
                            sectionId = article.sectionId,
                            createdAt = article.createdAt
                        )
                    }
            }
    }
}
