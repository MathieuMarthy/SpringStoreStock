package com.example.mms.controllers

import com.example.mms.dto.CartDTO
import com.example.mms.dto.ItemRequestDTO
import com.example.mms.dto.asCartDTO
import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.errors.ItemNotEnoughStockException
import com.example.mms.errors.UserNotFoundException
import com.example.mms.repository.CartRepository
import com.example.mms.services.ItemService
import com.example.mms.services.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@Validated
class CartController(
    private val cartRepository: CartRepository,
    private val itemService: ItemService,
    private val userService: UserService
) {

    @PostMapping("api/cart/{userId}")
    fun create(@PathVariable userId: String): ResponseEntity<CartDTO> {
        if (!this.userService.userExists(userId)) {
            throw UserNotFoundException(userId)
        }

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
    fun get(@PathVariable userId: String): ResponseEntity<CartDTO> {
        val cart = this.cartRepository.get(userId) ?: throw CartNotFoundException(userId)

        return ResponseEntity.ok(cart.asCartDTO())
    }

    @DeleteMapping("api/cart/{userId}")
    fun delete(@PathVariable userId: String): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.delete(userId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PostMapping("api/cart/{userId}/item")
    fun addItem(
        @PathVariable userId: String,
        @RequestBody @Valid updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        if (!this.itemService.haveEnoughStock(
                updateItemRequestDTO.itemId,
                updateItemRequestDTO.quantity
            )) {
            throw ItemNotEnoughStockException(updateItemRequestDTO.itemId)
        }

        val resCart = this.cartRepository.addItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)

        if (resCart.isFailure) {
            throw resCart.exceptionOrNull()!!
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PutMapping("api/cart/{userId}/item")
    fun updateItem(
        @PathVariable userId: String,
        @RequestBody updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        if (!this.itemService.haveEnoughStock(
                updateItemRequestDTO.itemId,
                updateItemRequestDTO.quantity
            )) {
            throw ItemNotEnoughStockException(updateItemRequestDTO.itemId)
        }

        val resCart = this.cartRepository.updateItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @DeleteMapping("api/cart/{userId}/item")
    fun deleteItem(@PathVariable userId: String, @RequestBody itemId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.deleteItem(userId, itemId)

        if (resCart.isFailure) {
            throw CartNotFoundException(userId)
        }

        return ResponseEntity.ok(resCart.getOrNull()!!.asCartDTO())
    }

    @PostMapping("api/cart/{userId}/valid")
    fun valid(@PathVariable userId: String): ResponseEntity<String> {
        val cart = this.cartRepository.get(userId) ?: throw CartNotFoundException(userId)

        if (cart.items.isEmpty()) {
            return ResponseEntity.ok("cart is empty")
        }

        if (!this.itemService.validItemsInCart(cart)) {
            return ResponseEntity.status(400).body("invalid items in cart")
        }

        this.cartRepository.valid(userId)
        this.userService.updateLastCommandDate(userId)
        return ResponseEntity.ok("valid")
    }
}
