package com.example.mms.Repository

import com.example.mms.Model.Item

interface ItemRepository {
    fun get(id : Int): Item?
    fun create(item: Item) : Result<Item>
    fun update(item: Item) : Result<Item>
    fun delete(item: Item) : Item?
}