package com.example.mms.Repository

import ItemNotEnoughStockException
import ItemNotFoundException
import com.example.mms.Controller.DTO.ItemInCartDTO
import com.example.mms.Model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ItemDatabaseRepository(private val jpa : ItemJpaRepository) : ItemRepository {
    override fun get(id: Int): Item? =
        jpa.findById(id).map { it.asItem() }.orElseGet(null)

    override fun getAll(): List<Item?> = jpa.findAll().map { it.asItem() }

    override fun create(item: Item): Result<Item> = if (jpa.findByName(item.name).isPresent) {
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
        val itemOptional = jpa.findById(id)
        if (itemOptional.isPresent) {
            jpa.deleteById(id)
            return itemOptional.get().asItem()
        }
        return null
    }

    override fun validAndDecreaseStock(items: List<ItemInCartDTO>) {
        val newItems = mutableListOf<Item>()
        for (item in items) {
            val itemInDb = jpa.findById(item.itemId).map { it.asItem() }
            if (itemInDb.isEmpty) {
                throw ItemNotFoundException(item.itemId)
            }

            if (itemInDb.get().stock < item.quantity) {
                throw ItemNotEnoughStockException(item.itemId)
            }
            newItems.add(itemInDb.get().copy(stock = itemInDb.get().stock - item.quantity))
        }

        for (item in newItems) {
            this.update(item)
        }
    }
}

interface ItemJpaRepository : JpaRepository<ItemEntity, Int> {
    fun findByName(name: String) : Optional<ItemEntity>
}