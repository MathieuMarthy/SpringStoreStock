package com.example.mms.Repository

import ItemNotEnoughStockException
import ItemNotFoundException
import com.example.mms.Controller.DTO.ItemInCartDTO
import com.example.mms.Model.Item
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.math.log

@Repository
class ItemDatabaseRepository(private val jpa : ItemJpaRepository) : ItemRepository {

    private val logger = LoggerFactory.getLogger(javaClass)
    override fun get(id: Int): Item? =
        jpa.findByIdOrNull(id)?.asItem()

    override fun getAll(): List<Item?> = jpa.findAll().map { it.asItem() }

    override fun create(item: Item): Result<Item> = if (jpa.findByName(item.name).isPresent) {
        logger.error("Item ${item.name} already in DB")
        Result.failure(Exception("Item already in DB"))
    } else {
        logger.info("Creating item ${item.name}")
        item.dateLastUpdate = Date()
        val saved = jpa.save(item.asEntity())
        Result.success(saved.asItem()!!)
    }

    override fun update(id : Int, item: Item): Result<Item> = if (jpa.findById(id).isPresent) {
        logger.info("Updating item ${item.name}")
        item.dateLastUpdate = Date()
        item.id = id
        val saved = jpa.save(item.asEntity())
        Result.success(saved.asItem()!!)
    } else {
        logger.error("Item ${item.id} not found")
        Result.failure(Exception("Item not in DB"))
    }

    override fun delete(id: Int): Item? {
        val itemOptional = jpa.findById(id)
        if (itemOptional.isPresent) {
            logger.info("Deleting item $id")
            jpa.deleteById(id)
            return itemOptional.get().asItem()
        }
        logger.error("Item $id not found")
        return null
    }

    override fun validAndDecreaseStock(items: List<ItemInCartDTO>) {
        val newItems = mutableListOf<Item>()
        for (item in items) {
            val itemInDb = jpa.findById(item.itemId).map { it.asItem() }
            if (itemInDb.isEmpty) {
                logger.error("Item ${item.itemId} not found")
                throw ItemNotFoundException(item.itemId)
            }

            if (itemInDb.get().stock < item.quantity) {
                logger.error("Item ${item.itemId} not enough stock")
                throw ItemNotEnoughStockException(item.itemId)
            }
            newItems.add(itemInDb.get().copy(stock = itemInDb.get().stock - item.quantity))
        }

        for (item in newItems) {
            logger.info("Decreasing stock of item ${item.name}")
            this.update(item.id, item)
        }
    }
}

interface ItemJpaRepository : JpaRepository<ItemEntity, Int> {
    fun findByName(name: String) : Optional<ItemEntity>
}