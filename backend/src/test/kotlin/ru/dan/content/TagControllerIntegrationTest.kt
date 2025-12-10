package ru.dan.content

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.http.MediaType
import ru.dan.content.model.TagCreateDto
import ru.dan.content.model.TagDto

@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var databaseClient: DatabaseClient

    companion object {
        @Container
        @JvmStatic
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:15-alpine")
            .apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                start()
            }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") {
                "r2dbc:postgresql://${postgresContainer.host}:${postgresContainer.firstMappedPort}/${postgresContainer.databaseName}?currentSchema=article"
            }
            registry.add("spring.r2dbc.username") { postgresContainer.username }
            registry.add("spring.r2dbc.password") { postgresContainer.password }
            registry.add("spring.flyway.url") {
                "jdbc:postgresql://${postgresContainer.host}:${postgresContainer.firstMappedPort}/${postgresContainer.databaseName}"
            }
        }
    }

    @BeforeAll
    fun setupTestData() {
        // Очищаем таблицу tag перед тестами
        databaseClient.sql("DELETE FROM tag").fetch().rowsUpdated().block()
    }

    @Test
    fun `should get all tags successfully`() {
        // Подготовка: добавляем тестовые теги
        databaseClient.sql("INSERT INTO tag (name) VALUES ('java'), ('kotlin')").fetch().rowsUpdated().block()

        // Выполняем тест
        webTestClient.get()
            .uri("/api/v1/tags")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(TagDto::class.java)
            .hasSize(2)
            .returnResult()
            .responseBody
            .let { tags ->
                val tagNames = tags?.map { it.name }
                assert("java" in tagNames!!)
                assert("kotlin" in tagNames)
            }
    }

    @Test
    fun `should get tag by valid ID successfully`() {
        // Подготовка: добавляем тег и получаем его ID
        databaseClient.sql("INSERT INTO tag (name) VALUES ('test-tag')").fetch().rowsUpdated().block()
        
        val tagId = databaseClient.sql("SELECT id FROM tag WHERE name = 'test-tag'").fetch()
            .first()
            .map { row -> row.get("id") as Long }
            .block()

        // Выполняем тест
        webTestClient.get()
            .uri("/api/v1/tags/$tagId")
            .exchange()
            .expectStatus().isOk
            .expectBody(TagDto::class.java)
            .consumeWith { response ->
                val tag = response.responseBody
                assert(tag?.id == tagId)
                assert(tag?.name == "test-tag")
            }
    }

    @Test
    fun `should return 400 when getting tag by invalid ID format`() {
        // Выполняем тест с неверным форматом ID
        webTestClient.get()
            .uri("/api/v1/tags/invalid-id")
            .exchange()
            .expectStatus().is4xxClientError // Ожидаем 400 ошибку
    }

    @Test
    fun `should return 400 when getting tag by negative ID`() {
        // Выполняем тест с отрицательным ID
        webTestClient.get()
            .uri("/api/v1/tags/-1")
            .exchange()
            .expectStatus().is4xxClientError // Ожидаем 400 ошибку
    }

//    @Test
//    fun `should create tag successfully with valid data`() {
//        // Подготовка данных для создания тега
//        val createDto = TagCreateDto(name = "spring-boot")
//
//        // Выполняем тест
//        webTestClient.post()
//            .uri("/api/v1/tags/create")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(createDto)
//            .exchange()
//            .expectStatus().is2xxSuccessful
//            .expectBody(TagDto::class.java)
//            .consumeWith { response ->
//                val tag = response.responseBody
//                assert(tag?.name == "spring-boot")
//                assert(tag?.id != null)
//            }
//    }
//
//    @Test
//    fun `should return 400 when creating tag with blank name`() {
//        // Подготовка данных с пустым именем
//        val createDto = TagCreateDto(name = "")
//
//        // Выполняем тест
//        webTestClient.post()
//            .uri("/api/v1/tags/create")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(createDto)
//            .exchange()
//            .expectStatus().is4xxClientError // Ожидаем 400 ошибку
//    }
//
//    @Test
//    fun `should return 400 when creating tag with null name`() {
//        // Подготовка данных с null именем (как JSON)
//        val createDto = """{"name": null}"""
//
//        // Выполняем тест
//        webTestClient.post()
//            .uri("/api/v1/tags/create")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(createDto)
//            .exchange()
//            .expectStatus().is4xxClientError // Ожидаем 400 ошибку
//    }
}