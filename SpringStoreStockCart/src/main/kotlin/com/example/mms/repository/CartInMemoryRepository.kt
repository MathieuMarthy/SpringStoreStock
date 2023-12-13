package com.example.mms.repository

import com.example.mms.errors.*
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart

// @Repository
class CartInMemoryRepository : CartRepository {

    private val carts = mutableMapOf<String, Cart>()

    override fun get(id: String): Cart {
        return this.carts[id] ?: throw CartNotFoundException(id)
    }

    override fun create(id: String): Cart {
        val cart = Cart(id)

        if (this.exists(id)) {
            throw CartAlreadyExistsException(id)
        }

        carts.putIfAbsent(id, cart)
        return cart
    }

    override fun addItem(id: String, itemId: Int, quantity: Int): Cart {
        if (!this.exists(id)) {
            throw CartNotFoundException(id)
        }

        val cart = this.get(id)
        val item = cart.items.find { i -> i.itemId == itemId }
        if (item != null) {
            throw ItemAlreadyExistsException(item.itemId)
        }

        cart.items.add(
            ItemInCart(
                itemId,
                quantity
            )
        )
        return cart
    }

    override fun updateItem(id: String, itemId: Int, quantity: Int): Cart {
        val cart = this.get(id)

        // Check if item is already in the items list
        val item = cart.items.find { i -> i.itemId == itemId } ?: throw ItemNotFoundException(id)

        // set the quantity
        item.quantity = quantity

        return cart
    }

        // check if item is in the items list
    override fun update(newCart: Cart): Cart {
        if (!this.exists(newCart.id)) {
            throw CartNotFoundException(newCart.id)
        }

        this.carts.replace(newCart.id, newCart)
        return this.get(newCart.id)
    }

    override fun deleteItem(id: String, itemId: Int): Cart {
        val cart = this.get(id)

        val item = cart.items.find { i -> i.itemId == itemId } ?: throw ItemNotFoundException(id)
        cart.items.remove(item)
        return cart
    }

    override fun delete(id: String): Cart {
        return carts.remove(id) ?: throw CartNotFoundException(id)
    }

    override fun valid(id: String): Boolean {
        val cart = this.get(id)

        cart.items.clear()
        return true
    }

    private fun exists(id: String) = this.carts.containsKey(id)
}
