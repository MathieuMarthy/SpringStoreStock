package com.example.mms.Repository

import com.example.mms.Model.Item
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class ItemDatabaseRepositoryTest : ItemDatabaseTest(){

    @Autowired
    private lateinit var jpa : ItemJpaRepository

    @BeforeEach
    fun setup() {
        repo = ItemDatabaseRepository(jpa)
        jpa.deleteAll()
    }


}