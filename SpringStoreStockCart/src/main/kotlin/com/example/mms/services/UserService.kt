package com.example.mms.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class UserService(
    @Value("\${url.userAPI}") private val userServiceUrl: String
) {

    fun updateLastCommandDate(userId: Int) {

    }
}
