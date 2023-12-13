package com.example.mms.Controller

import ItemNotFoundException
import com.example.mms.Controller.DTO.ItemDTO
import com.example.mms.Controller.DTO.ItemInCartDTO
import com.example.mms.Repository.ItemEntity
import com.example.mms.Repository.ItemRepository
import com.example.mms.Repository.asEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class ItemController(val itemRepository: ItemRepository)  {

    @Operation(summary = "Create a new item", description = "If the item already exists, it will return a 409 error", tags = ["Administration"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Item created",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemEntity::class))]),
        ApiResponse(responseCode = "409", description = "Item already exists",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PostMapping("api/items")
    fun create(@RequestBody @Valid item : ItemDTO): ResponseEntity<ItemEntity> =
        itemRepository.create(item.asItem()).fold(
            { s -> ResponseEntity.status(HttpStatus.CREATED).body(s.asEntity())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @Operation(summary = "Get an item by its id", description = "Get an item by its id", tags = ["Métier"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the item",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemEntity::class))]),
        ApiResponse(responseCode = "404", description = "Item not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @GetMapping("api/items/{id}")
    fun getOneItem(@PathVariable @Valid id : Int): ResponseEntity<ItemEntity> {
        val item = itemRepository.get(id) ?: throw ItemNotFoundException(id)
        return ResponseEntity.ok(item.asEntity())
    }

    @Operation(summary = "Get all items", description = "Get all items", tags = ["Métier"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the items",
            content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = ItemEntity::class)))]),
        ApiResponse(responseCode = "404", description = "Items not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @GetMapping("api/items")
    fun getAllItems(): ResponseEntity<List<ItemEntity>> =
        itemRepository.getAll()
            .map { it!!.asEntity() }
            .let { ResponseEntity.ok(it) }

    @Operation(summary = "Update an item by its id", description = "Update an item by its id", tags = ["Administration"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Item updated",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemEntity::class))]),
        ApiResponse(responseCode = "404", description = "Item not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Item already exists",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PutMapping("api/items/{id}")
    fun updateItem(@Valid id: Int, @RequestBody item : ItemDTO): ResponseEntity<Any> {
        val itemInDB = itemRepository.get(id)
        return if ( itemInDB == null) ResponseEntity.notFound().build()
        else itemRepository.update(id, item.asItem()).fold(
            { s -> ResponseEntity.ok(s.asEntity())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )
    }
    @Operation(summary = "Delete an item by its id", description = "Delete an item by its id", tags = ["Administration"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Item deleted",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemEntity::class))]),
        ApiResponse(responseCode = "404", description = "Item not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("api/items/{id}")
    fun deleteItem(@Valid id : Int): ResponseEntity<Any> =
        itemRepository.delete(id)?.let { ResponseEntity.noContent().build() } ?: throw ItemNotFoundException(id)

    @Operation(summary = "Valid and decrease stock of items", description = "Valid and decrease stock of items", tags = ["Métier"])
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Items valid and stock decreased",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = ItemEntity::class))]),
        ApiResponse(responseCode = "404", description = "Item not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Not enough stock",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PostMapping("api/items/validAndDecreaseStock")
    fun validAndDecreaseStock(@RequestBody @Valid items: List<ItemInCartDTO>): ResponseEntity<Any> {
        itemRepository.validAndDecreaseStock(items)
        return ResponseEntity.ok().build()
    }
}
