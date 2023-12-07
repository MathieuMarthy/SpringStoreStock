package com.example.mms.repository

import com.example.mms.Models.Cart

class CartInMemoryRepository: CartRepository {

    private val carts = mutableMapOf<Int,Cart>()
    override fun get(id : Int) = carts[id]
    override fun create(cart: Cart) : Result<Cart> {
        val prev = carts.putIfAbsent(cart.id, cart)
        return if (prev == null) {
            Result.success(cart)
        } else {
            Result.failure(Exception("Cart already exists"))
        }
    }
    override fun update(cart: Cart) : Result<Cart> {
        val updated = carts.replace(cart.id, cart)
        return if (updated == null) {
            Result.failure(Exception("Cart doesn't exist"))
        } else {
            Result.success(cart)
        }
    }
    override fun delete(cart: Cart) = carts.remove(cart.id)
}
