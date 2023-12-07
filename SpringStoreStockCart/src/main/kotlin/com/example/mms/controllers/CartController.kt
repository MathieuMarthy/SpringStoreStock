package com.example.mms.controllers

import com.example.mms.dto.CartDTO
import com.example.mms.dto.asCartDTO
import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.repository.CartRepository
import jakarta.validation.constraints.Min
import jakarta.websocket.server.PathParam
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
class CartController(val cartRepository: CartRepository) {

    @PostMapping("api/cart")
    fun create(@RequestBody userId: Int): ResponseEntity<CartDTO> {
        println("aaaaaaaaaaaaaaaa")

        val resCart = this.cartRepository.create(userId)

        if (resCart.isFailure) {
            throw CartAlreadyExistsException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @GetMapping("/api/cart/{userId}")
    fun get(@PathVariable userId: Int): ResponseEntity<CartDTO> {
        println("aaaaaaaaaaaaaaaa")
        val cart = this.cartRepository.get(userId) ?: throw CartNotFoundException(userId)

        return ResponseEntity.ok(cart.asCartDTO())
    }

    @DeleteMapping("api/cart")
    fun delete(@RequestBody userId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.delete(userId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PutMapping("api/cart/item")
    fun updateItem(@RequestBody userId: Int, itemId: Int, @Min(0) quantity: Int): ResponseEntity<CartDTO> {
        // TODO: vérifier le stock dans l'api des items
        val resCart = this.cartRepository.updateItem(userId, itemId, quantity)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @DeleteMapping("api/cart/item")
    fun deleteItem(@RequestBody userId: Int, itemId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.deleteItem(userId, itemId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PostMapping("api/cart/valid")
    fun valid(@RequestBody userId: Int): ResponseEntity.BodyBuilder {
        // TODO: vérifier le stock dans l'api des items
        this.cartRepository.valid(userId)

        // TODO: dans User changer la date de derniere commande
        return ResponseEntity.ok()
    }
}
