package com.example.mms.services

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

open class ServiceRequest {


    /**
     * Send a request to the given url
     *
     * @param url The url of the service
     * @param method The method of the request
     * @param body The body of the request
     * @return The response of the request
     */
    fun request(url: String, method: String, body: String = ""): HttpResponse<String> {
        val client = HttpClient.newBuilder().build()

        // build the request with the given url
        var requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")

        // set the method and body of the request
        requestBuilder = when (method) {
            "post" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body))
            "put" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body))
            "delete" -> requestBuilder.DELETE()
            else -> requestBuilder.GET()
        }

        val request = requestBuilder.build()

        // make the request and return the response
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}
