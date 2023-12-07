package com.example.mms.Repository

import com.example.mms.Controller.DTO.asDTO
import com.example.mms.Model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Repository
class ItemDatabaseRepository(private val jpa : ItemJpaRepository) : ItemRepository {
    override fun get(id: Int): Item? {
        return jpa.findById(id).map { it.asItem() }.get()
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