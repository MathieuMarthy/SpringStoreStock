package com.example.mms.repository

import com.example.mms.models.Cart

interface CartRepository {
    fun get(id: String): Cart
    fun create(id: String): Cart
    fun addItem(id: String, itemId: Int, quantity: Int): Cart
    fun updateItem(id: String, itemId: Int, quantity: Int): Cart
    fun deleteItem(id: String, itemId: Int): Cart
    fun update(newCart: Cart): Cart
    fun delete(id: String): Cart
    fun valid(id: String): Boolean
}
