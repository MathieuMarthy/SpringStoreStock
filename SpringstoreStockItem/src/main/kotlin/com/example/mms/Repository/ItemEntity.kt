package com.example.mms.Repository

import com.example.mms.Model.Item
import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Entity
@Table(name = "items")
data class ItemEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int? = null,
    val name : String,
    val price : Double,
    val stock : Int,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    val dateLastUpdate : Date?
    ){
    fun asItem(): Item? {
        return id?.let { Item(it, name, price, stock, dateLastUpdate) }
    }
}
fun Item.asEntity() = ItemEntity(id, name, price, stock, dateLastUpdate)