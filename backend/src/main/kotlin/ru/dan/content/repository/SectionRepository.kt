package ru.dan.content.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import ru.dan.content.entity.Section

interface SectionRepository : ReactiveCrudRepository<Section, Long>
