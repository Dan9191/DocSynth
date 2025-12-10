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
import ru.dan.content.model.SectionViewDto
import ru.dan.content.service.SectionService

@RestController
@RequestMapping("/api/v1/sections")
@Tag(name = "sections", description = "Operations related to Sections")
class SectionController(
    private val sectionService: SectionService

) {

    @GetMapping
    fun getAllTags(): Flux<SectionViewDto> {
        return sectionService.getAllSections()
    }

    @Operation(summary = "Get section by ID", description = "Get a specific section by its ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Section found",
                content = [Content(schema = Schema(implementation = ArticleViewDto::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Section not found",
                content = [Content()]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getSection(@PathVariable id: Long): Mono<SectionViewDto> {
        return sectionService.getSectionById(id)
    }
}
