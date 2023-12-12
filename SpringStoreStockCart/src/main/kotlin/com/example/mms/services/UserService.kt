package com.example.mms.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class UserService(
    @Value("\${url.userAPI}") private val userServiceUrl: String
): ServiceRequest() {

    fun updateLastCommandDate(userId: String): Boolean {
        val url = this.userServiceUrl + "/" + userId + "/updateLastCommandDate"

        val res = this.request(url, "put")

        return res.statusCode() == 200
    }
}
