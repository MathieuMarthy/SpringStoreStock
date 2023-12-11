package com.example.mms.Controller

import ItemNotFoundException
import com.example.mms.Controller.DTO.ItemDTO
import com.example.mms.Controller.DTO.asDTO
import com.example.mms.Repository.ItemEntity
import com.example.mms.Repository.ItemRepository
import com.example.mms.Repository.asEntity
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class ItemController(val itemRepository: ItemRepository)  {

    @PostMapping("api/items")
    fun create(@RequestBody @Valid item : ItemDTO): ResponseEntity<ItemDTO> =
        itemRepository.create(item.asItem()).fold(
            { s -> ResponseEntity.status(HttpStatus.CREATED).body(s.asDTO())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @GetMapping("api/items/{id}")
    fun getOneItem(@Valid id : Int): ResponseEntity<ItemDTO> {
        val item = itemRepository.get(id) ?: throw ItemNotFoundException(id)
        return ResponseEntity.ok(item.asDTO())
    }
    @GetMapping("api/items")
    fun getAllItems(): ResponseEntity<List<ItemEntity>> =
        itemRepository.getAll()
            .map { it!!.asEntity() }
            .let { ResponseEntity.ok(it) }

    @PutMapping("api/items/{id}")
    fun updateItem(@Valid id: Int, @RequestBody item : ItemDTO): ResponseEntity<Any> {
        val itemInDB = itemRepository.get(id)
        return if ( itemInDB == null) ResponseEntity.notFound().build()
        else itemRepository.update(item.asItem()).fold(
            { s -> ResponseEntity.ok(s.asDTO())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )
    }
    @DeleteMapping("api/items/{id}")
    fun deleteItem(@Valid id : Int): ResponseEntity<Any> =
        itemRepository.delete(id)?.let { ResponseEntity.noContent().build() } ?: throw ItemNotFoundException(id)
}