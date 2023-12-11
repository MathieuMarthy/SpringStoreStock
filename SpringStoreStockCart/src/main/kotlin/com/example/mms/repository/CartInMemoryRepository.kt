package com.example.mms.repository

import com.example.mms.errors.*
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart
import org.springframework.stereotype.Service

@Service
class CartInMemoryRepository : CartRepository {

    private val carts = mutableMapOf<Int, Cart>()

    override fun get(id: Int) = carts[id]

    override fun create(id: Int): Result<Cart> {
        val cart = Cart(id)

        val prev = carts.putIfAbsent(id, cart)

        return if (prev == null) {
            Result.success(cart)
        } else {
            Result.failure(CartAlreadyExistsException("Cart already exists"))
        }
    }

    override fun addItem(id: Int, itemId: Int, quantity: Int): Result<Cart> {
        if (!this.exists(id)) {
            return Result.failure(CartNotFoundException(id))
        }

        val cart = this.get(id)!!
        val item = cart.items.find { i -> i.itemId == itemId }
        if (item != null) {
            return Result.failure(ItemAlreadyExistsException(item.itemId))
        }

        cart.items.add(
            ItemInCart(
                itemId,
                quantity
            )
        )
        return Result.success(cart)
    }

    override fun updateItem(id: Int, itemId: Int, quantity: Int): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(CartNotFoundException(id))

        // Check if item is already in the items list
        val item = cart.items.find { i -> i.itemId == itemId } ?: return Result.failure(ItemNotFoundException(id))

        // set the quantity
        item.quantity = quantity

        // TODO: regarde si ca update bien, si le modifier cart change aussi cart dans la liste
        return Result.success(cart)
    }


    override fun update(newCart: Cart): Result<Cart> {
        if (this.exists(newCart.id)) {
            return Result.failure(CartNotFoundException(newCart.id))
        }

        this.carts.replace(newCart.id, newCart)
        return Result.success(this.get(newCart.id)!!)
    }

    override fun deleteItem(id: Int, itemId: Int): Result<Cart> {
        val cart = this.get(id) ?: return Result.failure(ItemNotFoundException(id))

        cart.items.removeIf { e -> e.itemId == itemId }
        return Result.success(cart)
    }

    override fun delete(id: Int): Result<Cart> {
        val cart = this.carts.remove(id) ?: return Result.failure(CartNotFoundException(id))

        return Result.success(cart)
    }

    override fun valid(id: Int): Boolean {
        val cart = this.get(id) ?: return false

        cart.items.clear()
        return true
    }

    private fun exists(id: Int) = this.carts.containsKey(id)
}
