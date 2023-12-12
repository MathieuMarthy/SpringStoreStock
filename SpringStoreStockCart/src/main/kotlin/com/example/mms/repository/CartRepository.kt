package com.example.mms.repository

import com.example.mms.models.Cart

interface CartRepository {
    fun get(id: String): Cart?
    fun create(id: String): Result<Cart>
    fun addItem(id: String, itemId: Int, quantity: Int): Result<Cart>
    fun updateItem(id: String, itemId: Int, quantity: Int): Result<Cart>
    fun deleteItem(id: String, itemId: Int): Result<Cart>
    fun update(newCart: Cart): Result<Cart>
    fun delete(id: String): Result<Cart>
    fun valid(id: String): Boolean
}
