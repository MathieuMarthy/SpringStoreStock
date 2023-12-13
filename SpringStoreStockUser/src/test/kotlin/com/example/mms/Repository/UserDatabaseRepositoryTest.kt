package com.example.mms.Repository


import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserDatabaseRepositoryTest : UserDatabaseTest() {
        @Autowired
        private lateinit var jpa : UserJpaRepository

        @BeforeEach
        fun setup() {
            repo = UserDatabaseRepository(jpa)
            jpa.deleteAll()
        }

}