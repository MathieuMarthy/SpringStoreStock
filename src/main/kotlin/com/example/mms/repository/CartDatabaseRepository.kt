package com.example.mms.repository

import com.example.mms.Models.Cart
import org.springframework.data.jpa.repository.JpaRepository

class CartDatabaseRepository(private val jpa: CartJpaRepository): CartRepository {
    override fun get(): Cart {
        TODO("Not yet implemented")
    }

    override fun create(cart: Cart) {
        TODO("Not yet implemented")
    }

    override fun update(cart: Cart) {
        TODO("Not yet implemented")
    }

    override fun delete(cart: Cart) {
        TODO("Not yet implemented")
    }

}

interface CartJpaRepository : JpaRepository<Cart, Int>
