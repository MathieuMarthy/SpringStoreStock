package com.example.mms.Repository

import ItemNotFoundException
import com.example.mms.Model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ItemDatabaseRepository(private val jpa : ItemJpaRepository) : ItemRepository {
    override fun get(id: Int): Item? {
        val item = jpa.findById(id).map { it.asItem() }
        if (item.isEmpty) {
            return null
        }

        return item.get()
    }

    override fun getAll(): List<Item?> = jpa.findAll().map { it.asItem() }

    override fun create(item: Item): Result<Item> = if (jpa.findById(item.id).isPresent) {
        Result.failure(Exception("Item already in DB"))
    } else {
        item.dateLastUpdate = Date()
        val saved = jpa.save(item.asEntity())
        Result.success(saved.asItem()!!)
    }

    override fun update(item: Item): Result<Item> = if (jpa.findById(item.id).isPresent) {
        item.dateLastUpdate = Date()
        val saved = jpa.save(item.asEntity())
        Result.success(saved.asItem()!!)
    } else {
        Result.failure(Exception("Item not in DB"))
    }

    override fun delete(id: Int): Item? {
        return jpa.findById(id)
            .also { jpa.deleteById(id) }
            .map { it.asItem() }.get()
    }
}

interface ItemJpaRepository : JpaRepository<ItemEntity, Int>