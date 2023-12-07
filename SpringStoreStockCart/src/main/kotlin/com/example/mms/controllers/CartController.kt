package com.example.mms.controllers

import com.example.mms.dto.CartDTO
import com.example.mms.dto.ItemRequestDTO
import com.example.mms.dto.asCartDTO
import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.repository.CartRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@Validated
class CartController(val cartRepository: CartRepository) {

    @PostMapping("api/cart/{userId}")
    fun create(@PathVariable userId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.create(userId)

        if (resCart.isFailure) {
            throw CartAlreadyExistsException(userId)
        }

        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand()
            .toUri()
        return ResponseEntity.created(location).body(resCart.getOrNull()!!.asCartDTO())
    }

    @GetMapping("api/cart/{userId}")
    fun get(@PathVariable userId: Int): ResponseEntity<CartDTO> {
        val cart = this.cartRepository.get(userId) ?: throw CartNotFoundException(userId)

        return ResponseEntity.ok(cart.asCartDTO())
    }

    @DeleteMapping("api/cart/{userId}")
    fun delete(@PathVariable userId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.delete(userId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PostMapping("api/cart/{userId}/item")
    fun addItem(
        @PathVariable userId: Int,
        @RequestBody @Valid updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.addItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)

        if (resCart.isFailure) {
            throw resCart.exceptionOrNull()!!
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PutMapping("api/cart/{userId}/item")
    fun updateItem(
        @PathVariable userId: Int,
        @RequestBody updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        // TODO: vérifier le stock dans l'api des items
        val resCart = this.cartRepository.updateItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @DeleteMapping("api/cart/{userId}/item")
    fun deleteItem(@PathVariable userId: Int, @RequestBody itemId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.deleteItem(userId, itemId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PostMapping("api/cart/{userId}/valid")
    fun valid(@PathVariable userId: Int): ResponseEntity.BodyBuilder {
        // TODO: vérifier le stock dans l'api des items
        this.cartRepository.valid(userId)

        // TODO: dans User changer la date de derniere commande
        return ResponseEntity.ok()
    }
}
