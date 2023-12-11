package com.example.mms.services

import com.example.mms.dto.ItemDTO
import com.example.mms.errors.ItemNotFoundException
import com.example.mms.models.Cart
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Service
class ItemService(
    @Value("\${url.itemAPI}") private val itemServiceUrl: String
) {

    private val json = Json { ignoreUnknownKeys = true }

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

    fun getItems() {
        val url = this.itemServiceUrl

        val res = this.request(url, "get")


        println(res.body())
    }


    fun haveEnoughStock(itemId: Int, quantity: Int): Boolean {
        val url = this.itemServiceUrl + "/" + itemId

        val res = this.request(url, "get")
        if (res.statusCode() == 404) {
            throw ItemNotFoundException(itemId)
        }

        val itemDTO = this.json.decodeFromString<ItemDTO>(res.body())

        return itemDTO.stock >= quantity
    }

    fun validItemsInCart(cart: Cart): List<Int> {
        TODO()
    }

    fun decreaseStock(itemsId: List<Int>, quantities: List<Int>): List<Int> {
        TODO()
    }
}
