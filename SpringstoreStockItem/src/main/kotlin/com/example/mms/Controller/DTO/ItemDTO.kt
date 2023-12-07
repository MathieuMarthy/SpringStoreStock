package com.example.mms.Controller.DTO

import com.example.mms.Model.Item
import jakarta.validation.constraints.Size


data class ItemDTO(
    @field:Size(min = 2, max = 30) val name: String,
    val price: Double,
    val stock: Int
){
    fun asItem() = Item(0, name, price, stock, null)
}

fun Item.asDTO() = ItemDTO(this.name, this.price, this.stock)