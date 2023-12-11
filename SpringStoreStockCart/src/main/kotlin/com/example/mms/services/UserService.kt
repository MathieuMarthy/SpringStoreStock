package com.example.mms.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.stereotype.Service


@Service
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class UserService(
    @Value("\${url.userAPI}") private val userServiceUrl: String
) {

    fun updateLastCommandDate(userId: Int) {

    }
}
