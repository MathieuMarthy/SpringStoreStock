package com.example.mms.services

import com.example.mms.models.Cart
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Service
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class ItemService(
    @Value("\${url.itemAPI}") private val itemServiceUrl: String
) {

    private fun request(url: String, method: String, body: String = ""): HttpResponse<String> {
        val client = HttpClient.newBuilder().build();

        var requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))

        requestBuilder = when (method) {
            "post" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body))
            "put" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body))
            "delete" -> requestBuilder.DELETE()
            else -> requestBuilder.GET()
        }

        val request = requestBuilder.build()

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    fun haveEnoughStock(itemId: Int, quantity: Int): Boolean {
        val url = this.itemServiceUrl + itemId

        val res = this.request(url, "get")

        return res.statusCode() == 200
    }

    fun validItemsInCart(cart: Cart): List<Int> {
        TODO()
    }

    fun decreaseStock(itemsId: List<Int>, quantities: List<Int>): List<Int> {
        TODO()
    }
}
