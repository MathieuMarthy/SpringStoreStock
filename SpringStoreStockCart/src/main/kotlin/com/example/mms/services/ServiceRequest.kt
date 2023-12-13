package com.example.mms.services

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class ServiceRequest {
    fun request(url: String, method: String, body: String = ""): HttpResponse<String> {
        val client = HttpClient.newBuilder().build()

        var requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")

        requestBuilder = when (method) {
            "post" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body))
            "put" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body))
            "delete" -> requestBuilder.DELETE()
            else -> requestBuilder.GET()
        }

        val request = requestBuilder.build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}
