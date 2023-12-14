package com.example.mms.controllers

import com.example.mms.dto.CartDTO
import com.example.mms.dto.ItemRequestDTO
import com.example.mms.dto.asCartDTO
import com.example.mms.errors.CartNotFoundException
import com.example.mms.errors.ItemNotEnoughStockException
import com.example.mms.errors.UserNotFoundException
import com.example.mms.repository.CartRepository
import com.example.mms.services.ItemService
import com.example.mms.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(summary = "Create a cart for a user", description = "Create a cart for a user, the user must exist", tags = ["Administration"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Cart created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "409", description = "Cart already exists",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("api/cart/{userId}")
    fun create(@PathVariable userId: String): ResponseEntity<CartDTO> {
        // check if user exists
        if (!this.userService.userExists(userId)) {
            throw UserNotFoundException(userId)
        }

        val resCart = this.cartRepository.create(userId)

        // get the location of the created cart
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .buildAndExpand()
            .toUri()

        return ResponseEntity.created(location).body(resCart.asCartDTO())
    }

    @Operation(summary = "Get a cart for a user", description = "Get a cart for a user", tags = ["Administration"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User's cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("api/cart/{userId}")
    fun get(@PathVariable userId: String): ResponseEntity<CartDTO> {
        val cart = this.cartRepository.get(userId)
        return ResponseEntity.ok(cart.asCartDTO())
    }

    @Operation(summary = "Delete a cart for a user", description = "Delete a cart for a user", tags = ["Administration"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "the deleted cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @DeleteMapping("api/cart/{userId}")
    fun delete(@PathVariable userId: String): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.delete(userId)
        return ResponseEntity.ok(resCart.asCartDTO())
    }

    @Operation(
        summary = "Add an item to a cart",
        description = "Add an item to a cart, the item must exist and the quantity must be available",
        tags = ["Métier"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "the updated cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "409", description = "Item not enough stock",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("api/cart/{userId}/item")
    fun addItem(
        @PathVariable userId: String,
        @RequestBody @Valid updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        // check if item has enough stock in the item api
        if (!this.itemService.haveEnoughStock(
                updateItemRequestDTO.itemId,
                updateItemRequestDTO.quantity
            )
        ) {
            throw ItemNotEnoughStockException(updateItemRequestDTO.itemId)
        }

        // add item to cart and return the created cart
        val resCart = this.cartRepository.addItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)
        return ResponseEntity.ok(resCart.asCartDTO())
    }

    @Operation(
        summary = "Update an item in a cart",
        description = "Update an item in a cart, the item must exist and the quantity must be available",
        tags = ["Métier"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "the updated cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404", description = "Item not Found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "409", description = "Item not enough stock",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PutMapping("api/cart/{userId}/item")
    fun updateItem(
        @PathVariable userId: String,
        @RequestBody updateItemRequestDTO: ItemRequestDTO
    ): ResponseEntity<CartDTO> {
        // check if item has enough stock in the item api
        if (!this.itemService.haveEnoughStock(
                updateItemRequestDTO.itemId,
                updateItemRequestDTO.quantity
            )
        ) {
            throw ItemNotEnoughStockException(updateItemRequestDTO.itemId)
        }

        // update item in cart and return the updated cart
        val resCart = this.cartRepository.updateItem(userId, updateItemRequestDTO.itemId, updateItemRequestDTO.quantity)
        return ResponseEntity.ok(resCart.asCartDTO())
    }

    @Operation(summary = "Delete an item in a cart", description = "Delete an item in a cart", tags = ["Métier"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "the deleted cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404", description = "Item not Found",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @DeleteMapping("api/cart/{userId}/item")
    fun deleteItem(@PathVariable userId: String, @RequestBody itemId: Int): ResponseEntity<CartDTO> {
        val resCart = this.cartRepository.deleteItem(userId, itemId)
        return ResponseEntity.ok(resCart.asCartDTO())
    }

    @Operation(
        summary = "Valid a cart",
        description = "Valid a cart, the cart must not be empty and all items must be valid",
        tags = ["Métier"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "the valid cart",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CartDTO::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Cart not found",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "404", description = "Item not Found",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("api/cart/{userId}/valid")
    fun valid(@PathVariable userId: String): ResponseEntity<String> {
        // get the cart, throw exception if not found
        val cart = this.cartRepository.get(userId)

        if (cart.items.isEmpty()) {
            return ResponseEntity.ok("cart is empty")
        }

        // call the item api to check if all items have enough stock and decrease the stock
        if (!this.itemService.validItemsInCart(cart)) {
            throw CartNotFoundException(userId)
        }

        // clear the cart
        this.cartRepository.valid(userId)

        // update the last command date of the user
        this.userService.updateLastCommandDate(userId)
        return ResponseEntity.ok("valid")
    }
}
