package ru.dan.content.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.dan.content.model.SectionViewDto
import ru.dan.content.model.TagDto
import ru.dan.content.repository.SectionRepository
import ru.dan.content.repository.TagRepository

private val logger = KotlinLogging.logger {}

/**
 * Сервис работы с секциями.
 */
@Service
class SectionService(
    private val sectionRepository: SectionRepository,
    private val tagRepository: TagRepository
) {

    /**
     * Поиск секции по id.
     *
     * @param id идентификатор секции
     * @return Mono<SectionViewDto> — либо секция, либо пустой Mono, если не найдено
     */
    @Transactional
    fun getSectionById(id: Long): Mono<SectionViewDto> {
        return sectionRepository.findById(id)
            .flatMap { section ->
                tagRepository.findAllBySectionId(section.id!!)
                    .map { tag -> TagDto(tag.id, tag.name) }
                    .collectList()
                    .map { tags ->
                        SectionViewDto(
                            id = section.id,
                            name = section.name,
                            description = section.description,
                            parentId = section.parentId,
                            tags = tags
                        )
                    }
            }
    }

    @Transactional
    fun getAllSections(): Flux<SectionViewDto> {
        return sectionRepository.findAll()
            .flatMap { section ->
                tagRepository.findAllBySectionId(section.id!!)
                    .map { tag -> TagDto(tag.id, tag.name) }
                    .collectList()
                    .map { tags ->
                        SectionViewDto(
                            id = section.id,
                            name = section.name,
                            description = section.description,
                            parentId = section.parentId,
                            tags = tags
                        )
                    }
            }
    }
}
