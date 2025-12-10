package ru.dan.content.service

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux
import ru.dan.content.model.TagCreateDto
import ru.dan.content.model.TagDto
import ru.dan.content.entity.Tag
import ru.dan.content.repository.TagRepository

private val logger = KotlinLogging.logger {}

/**
 * Tag service.
 */
@Service
class TagService(
    private val tagRepository: TagRepository
) {

    /**
     * Retrieves all tags from the database.
     *
     * @return [Flux] emitting all [TagDto] entities
     */
    @Transactional
    fun getAllTags(): Flux<TagDto> {
        logger.info { "Fetching all tags" }
        return tagRepository.findAll()
            .map { entity ->
                TagDto(
                    id = entity.id,
                    name = entity.name
                )
            }
            .doOnError { e -> logger.error(e) { "Error fetching tags" } }
    }

    /**
     * Retrieves a tag by its unique identifier.
     *
     * @param idStr the ID of the tag to retrieve
     * @return [Mono] emitting the [TagDto] if found, or empty if not
     */
    @Transactional
    fun getTagById(idStr: String): Mono<TagDto> {
        logger.info { "Fetching tag by id: $idStr" }

        val id = try {
            idStr.toLong()
        } catch (e: NumberFormatException) {
            logger.warn { "Invalid ID format: '$idStr'" }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID format")
        }

        if (id <= 0) {
            logger.warn { "ID must be positive: $id" }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be positive")
        }

        logger.info { "Fetching tag by id: $id" }
        return tagRepository.findById(id)
            .map { entity ->
                TagDto(
                    id = entity.id,
                    name = entity.name
                )
            }
            .doOnError { e -> logger.error(e) { "Error fetching tag by id: $id-" } }
    }

    /**
     * Creates a new tag in the database.
     *
     * @param dto the [TagDto] entity to persist
     * @return [Mono] emitting the saved [Tag] with generated ID
     */
    @Transactional
    fun createTag(dto: TagCreateDto): Mono<TagDto> {
        logger.info { "Creating new tag: ${dto.name}" }
        val tag = Tag(name = dto.name)
        return tagRepository.save(tag)
            .map { entity ->
                TagDto(id = entity.id, name = entity.name)
            }
            .doOnSuccess { saved -> logger.info { "Tag created successfully: id=${saved.id}" } }
            .doOnError { e -> logger.error(e) { "Failed to create tag: ${dto.name}" } }
    }
}