package com.example.mms.repository

import com.example.mms.models.Cart

interface CartRepository {
    fun get(id: Int): Cart?
    fun create(id: Int): Result<Cart>
    fun updateItem(id: Int, itemId: Int, quantity: Int): Result<Cart>
    fun deleteItem(id: Int, itemId: Int): Result<Cart>
    fun update(newCart: Cart): Result<Cart>
    fun delete(id: Int): Result<Cart>
    fun valid(id: Int): Boolean
}
