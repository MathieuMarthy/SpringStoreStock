package com.example.mms.Repository

import com.example.mms.Model.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class ItemDatabaseRepository(private val jpa : ItemJpaRepository) : ItemRepository {
    override fun get(id: Int): Item? {
        return jpa.findById(id).orElse(null)
    }

    override fun create(item: Item): Result<Item> {
        return Result.success(jpa.save(item))
    }

    override fun update(item: Item): Result<Item> {
        return Result.success(jpa.save(item))
    }

    override fun delete(item: Item): Item? {
        jpa.delete(item)
        return item
    }
}

interface ItemJpaRepository : JpaRepository<Item, Int>