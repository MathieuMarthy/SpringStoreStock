package com.example.mms.Repository


import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
class ItemDatabaseRepositoryTest : ItemDatabaseTest(){

    @Autowired
    private lateinit var jpa : ItemJpaRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    @BeforeEach
    fun setup() {
        repo = ItemDatabaseRepository(jpa)
        jpa.deleteAll()
        jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN id RESTART WITH 1")
    }


}