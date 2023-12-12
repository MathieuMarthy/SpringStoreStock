package com.example.mms.repository

import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.errors.ItemAlreadyExistsException
import com.example.mms.errors.ItemNotFoundException
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

class CartDatabaseRepository(private val jpa: CartJpaRepository) : CartRepository {
    override fun get(id: String): Cart? {
        return this.jpa.findByIdOrNull(id)
    }

    override fun create(id: String): Result<Cart> {
        this.get(id) ?: return Result.failure(CartNotFoundException(id))

        val cart = Cart(id)
        return Result.success(this.jpa.save(cart))
    }

    override fun addItem(id: String, itemId: Int, quantity: Int): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(CartNotFoundException(id))

        // Check if item is already in the items list
        val item = cart.items.find { i -> i.itemId == itemId }

        if (item != null) {
            return Result.failure(ItemAlreadyExistsException(id))
        }

        cart.items.add(
            ItemInCart(
                itemId,
                quantity
            )
        )

        return Result.success(this.jpa.save(cart))
    }

    override fun updateItem(id: String, itemId: Int, quantity: Int): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(CartAlreadyExistsException(id))

        val item = cart.items.find { i -> i.itemId == itemId } ?: return Result.failure(ItemNotFoundException(id))
        item.quantity = quantity

        return Result.success(this.jpa.save(cart))
    }

    override fun deleteItem(id: String, itemId: Int): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(CartNotFoundException(id))

        val item = cart.items.find { i -> i.itemId == itemId } ?: return Result.failure(ItemNotFoundException(id))
        cart.items.remove(item)

        return Result.success(this.jpa.save(cart))
    }

    override fun update(newCart: Cart): Result<Cart> {
        this.get(newCart.id) ?: return Result.failure(CartNotFoundException(newCart.id))
        return Result.success(this.jpa.save(newCart))
    }

    override fun delete(id: String): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(CartNotFoundException(id))
        this.jpa.delete(cart)
        return Result.success(cart)
    }

    override fun valid(id: String): Boolean {
        val cart = this.get(id) ?: throw CartNotFoundException(id)

        cart.items.clear()
        this.jpa.save(cart)
        return true
    }
}

interface CartJpaRepository : JpaRepository<Cart, String>
