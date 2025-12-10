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
import ru.dan.content.model.TagDto

@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagIntegrationTest {

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
            registry.add("spring.flyway.user") { postgresContainer.username }
            registry.add("spring.flyway.password") { postgresContainer.password }
        }

    }

    @BeforeAll
    fun setupTestData() {

        val scriptContent = javaClass.classLoader.getResource("insert_test_tag.sql")?.readText()
        databaseClient.sql(scriptContent ?: "").fetch().rowsUpdated().block()
    }

    @Test
    fun `should find the tag added by the script`() {
        // Проверяем, что тег, добавленный через скрипт в BeforeAll, доступен через API
        webTestClient.get()
            .uri("/api/v1/tags")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(TagDto::class.java)
            .hasSize(1)
            .returnResult()
            .responseBody
            .let { tags ->
                val foundTag = tags?.firstOrNull { tag -> tag.name == "test-tag" }
                assert(foundTag != null) { "Expected to find tag with name 'test-tag'" }
                assert(foundTag?.name == "test-tag") { "Expected tag name to be 'test-tag'" }
            }
    }
}