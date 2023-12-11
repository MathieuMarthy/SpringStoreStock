package com.example.mms.Repository

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.example.mms.Model.Item
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

open class ItemDatabaseTest {

    private fun defaultItem(
        id : Int = 1,
        name : String = "test",
        price : Double = 1.0,
        stock : Int = 1,
        dateLastUpdate : Date? = null
    ) : Item {
        return Item(id, name, price, stock, dateLastUpdate)
    }

    lateinit var repo : ItemDatabaseRepository

    @Test
    fun `create once working`() {
        val item = defaultItem()
        val result = repo.create(item)
        assertThat(result).isSuccess()
        assertThat(result.getOrNull()!!.id).isEqualTo(item.id)
    }
    @Test
    fun `create twice not working`() {
        val item = defaultItem()
        repo.create(item)
        val result = repo.create(item)
        assertThat(result).isFailure()
    }

    @Test
    fun `create twice with different name working`() {
        val item = defaultItem()
        repo.create(item)
        val result = repo.create(item.copy(name = "test2"))
        assertThat(result).isSuccess()
    }

    @Test
    fun `get one working`() {
        val item = defaultItem()
        repo.create(item)
        val result = repo.get(item.id)
        assertThat(result!!.id).isEqualTo(item.id)
    }

    @Test
    fun `get one not working`() {
        val item = defaultItem()
        val result = repo.get(item.id)
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `get all working`() {
        val item = defaultItem()
        val item2 = defaultItem(id = 2, name = "test2")
        val item3 = defaultItem(id = 3, name = "test3")
        repo.create(item)
        repo.create(item2)
        repo.create(item3)
        val result = repo.getAll()
        assertThat(result).containsExactlyInAnyOrder(item, item2, item3)
    }
    @Test
    fun `get all not working`() {
        val result = repo.getAll()
        assertThat(result).containsExactlyInAnyOrder()
    }

    @Test
    fun `update an existing item`(){
        val item = defaultItem()
        repo.create(item)
        val result = repo.update(item.copy(name = "test2"))
        assertThat(result).isSuccess()
        assertThat(result.getOrNull()!!.name).isEqualTo("test2")
    }

    @Test
    fun `update a non existing item`(){
        val item = defaultItem()
        val result = repo.update(item)
        assertThat(result).isFailure()
    }

    @Test
    fun `delete existing item`() {
        val item = defaultItem()
        repo.create(item)
        assertThrows<Exception> {
            repo.delete(item.id)
        }
    }

    @Test
    fun `delete non existing item`() {
        val item = defaultItem()
        val result = repo.delete(item.id)
        assertThat(result).isEqualTo(null)
    }

}