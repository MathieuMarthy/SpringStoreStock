package com.example.mms.repository

import com.example.mms.models.Cart
import org.springframework.data.jpa.repository.JpaRepository

class CartDatabaseRepository(private val jpa: CartJpaRepository) : CartRepository {
    override fun get(id: String): Cart? {
        TODO("Not yet implemented")
    }

    override fun create(id: String): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun addItem(id: String, itemId: Int, quantity: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun updateItem(id: String, itemId: Int, quantity: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun deleteItem(id: String, itemId: Int): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun update(newCart: Cart): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun delete(id: String): Result<Cart> {
        TODO("Not yet implemented")
    }

    override fun valid(id: String): Boolean {
        TODO("Not yet implemented")
    }

}

interface CartJpaRepository : JpaRepository<Cart, Int>
