package com.example.mms.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CartDatabaseRepositoryTest : CartDatabaseTest() {

    @Autowired
    private lateinit var jpa: CartJpaRepository

    @BeforeEach
    fun setUp() {
        repository = CartDatabaseRepository(jpa)
        jpa.deleteAll()
    }
}
