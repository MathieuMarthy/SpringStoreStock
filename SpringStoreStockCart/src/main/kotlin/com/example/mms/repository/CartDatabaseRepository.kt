package com.example.mms.repository

import com.example.mms.models.Cart
import org.springframework.data.jpa.repository.JpaRepository

class CartDatabaseRepository(private val jpa: CartJpaRepository) : CartRepository {
    override fun get(id: Int): Cart? {
        TODO("Not yet implemented")
    }

    override fun create(cart: Cart): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun update(cart: Cart): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun delete(cart: Cart): Cart? {
        TODO("Not yet implemented")
    }
}

interface CartJpaRepository : JpaRepository<Cart, Int>
