package com.example.mms.repository

import com.example.mms.models.Cart
import org.springframework.data.jpa.repository.JpaRepository

class CartDatabaseRepository(private val jpa: CartJpaRepository) : CartRepository {
    override fun get(id: Int): Cart? {
        TODO("Not yet implemented")
    }

    override fun create(id: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun addItem(id: Int, itemId: Int, quantity: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun updateItem(id: Int, itemId: Int, quantity: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun deleteItem(id: Int, itemId: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun update(newCart: Cart): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun valid(id: Int): Boolean {
        TODO("Not yet implemented")
    }

}

interface CartJpaRepository : JpaRepository<Cart, Int>
