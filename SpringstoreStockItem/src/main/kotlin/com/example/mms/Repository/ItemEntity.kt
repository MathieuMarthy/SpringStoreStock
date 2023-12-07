package com.example.mms.Repository

import com.example.mms.Model.Item
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "items")
data class ItemEntity (
    @Id val id : Int,
    val name : String,
    val price : Double,
    val stock : Int,
    val dateLastUpdate : Date
    ){
    fun asItem() = Item(id, name, price, stock, dateLastUpdate)
}
fun Item.asEntity() = ItemEntity(id, name, price, stock, dateLastUpdate)