package com.example.mms.dto

import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart

class CartDTO(
        val id: String,
        val items: MutableList<ItemInCart> = mutableListOf()
) {
    fun asCart() = Cart(this.id, this.items)
}

fun Cart.asCartDTO() = CartDTO(this.id, this.items)
