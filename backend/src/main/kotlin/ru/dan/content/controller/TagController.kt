package ru.dan.content.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.dan.content.model.TagCreateDto
import ru.dan.content.model.TagDto
import ru.dan.content.service.TagService

@RestController
@RequestMapping("/api/v1/tags")
@Tag(name = "tag", description = "Operations related to Tag")
@Validated
class TagController(
    private val tagService: TagService
) {

    /**
     * Get all tags.
     */
    @Operation(
        summary = "Get all tags",
        description = "Returns a stream of all tags in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved list of tags",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TagDto::class)
                )]
            )
        ]
    )
    @GetMapping
    fun getAllTags(): Flux<TagDto> {
        return tagService.getAllTags()
    }

    /**
     * Get all tags.
     */
    @Operation(
        summary = "Get tag by ID",
        description = "Retrieves a single tag by its unique identifier. ID must be a positive number."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Tag found successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TagDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID format or ID is not positive",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Tag with specified ID not found",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getTagsById (@PathVariable id: String): Mono<TagDto> {
        return tagService.getTagById(id)
    }

    // todo выключено до реализации SSO
//    /**
//     * Create a new Tag.
//     */
//    @Operation(
//        summary = "Create a new tag",
//        description = "Creates a new tag with the provided name. Name must not be blank."
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "201",
//                description = "Tag created successfully",
//                content = [Content(
//                    mediaType = "application/json",
//                    schema = Schema(implementation = TagDto::class)
//                )]
//            ),
//            ApiResponse(
//                responseCode = "400",
//                description = "Invalid request body or name is blank",
//                content = [Content(mediaType = "application/json")]
//            )
//        ]
//    )
//    @PostMapping("create")
//    fun createTag(
//        @Parameter(
//            description = "Tag creation payload",
//            required = true
//        )
//        @Valid @RequestBody dto: TagCreateDto): Mono<TagDto> {
//        return tagService.createTag(dto)
//    }
}
