package com.example.mms.repository

import com.example.mms.Models.Cart

interface CartRepository {

    fun get(): Cart

    fun create(cart: Cart)

    fun update(cart: Cart)

    fun delete(cart: Cart)
}
