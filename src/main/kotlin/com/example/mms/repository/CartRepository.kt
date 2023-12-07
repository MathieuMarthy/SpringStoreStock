package com.example.mms.repository

import com.example.mms.Models.Cart

interface CartRepository {
    fun get(id : Int): Cart?
    fun create(cart: Cart) : Result<Cart>
    fun update(cart: Cart) : Result<Cart>
    fun delete(cart: Cart) : Cart?
}
