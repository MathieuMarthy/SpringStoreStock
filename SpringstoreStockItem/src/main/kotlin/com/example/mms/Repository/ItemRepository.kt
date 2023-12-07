package com.example.mms.Repository

import com.example.mms.Model.Item

interface ItemRepository {
    fun get(id : Int): Item?
    fun getAll(): List<Item?>
    fun create(item: Item) : Result<Item>
    fun update(item: Item) : Result<Item>
    fun delete(id: Int) : Item?
}