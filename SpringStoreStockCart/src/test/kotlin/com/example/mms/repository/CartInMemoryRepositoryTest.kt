package com.example.mms.repository

import org.junit.jupiter.api.BeforeEach

class CartInMemoryRepositoryTest : CartDatabaseTest() {

    @BeforeEach
    fun setUp() {
        repository = CartInMemoryRepository()
    }
}
