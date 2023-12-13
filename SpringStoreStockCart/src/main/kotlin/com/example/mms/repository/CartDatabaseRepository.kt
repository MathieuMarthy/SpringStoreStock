package com.example.mms.repository

import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.errors.ItemAlreadyExistsException
import com.example.mms.errors.ItemNotFoundException
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CartDatabaseRepository(private val jpa: CartJpaRepository) : CartRepository {
    override fun get(id: String): Cart {
        return jpa.findByIdOrNull(id) ?: throw CartNotFoundException(id)
    }

    override fun create(id: String): Cart {
        if (this.jpa.existsById(id)) {
            throw CartAlreadyExistsException(id)
        }
        val cart = Cart(id)
        return this.jpa.save(cart)
    }

    override fun addItem(id: String, itemId: Int, quantity: Int): Cart {
        val cart = this.get(id)

        // Check if item is already in the items list
        val item = cart.items.find { i -> i.itemId == itemId }

        if (item != null) {
            throw ItemAlreadyExistsException(id)
        }

        cart.items.add(
                ItemInCart(
                        itemId,
                        quantity
                )
        )

        return this.jpa.save(cart)
    }

    override fun updateItem(id: String, itemId: Int, quantity: Int): Cart {
        val cart = this.get(id)

        val item = cart.items.find { i -> i.itemId == itemId } ?: throw ItemNotFoundException(id)
        item.quantity = quantity

        return this.jpa.save(cart)
    }

    override fun deleteItem(id: String, itemId: Int): Cart {
        val cart = this.get(id)

        val item = cart.items.find { i -> i.itemId == itemId } ?: throw ItemNotFoundException(id)
        cart.items.remove(item)

        return this.jpa.save(cart)
    }

    override fun update(newCart: Cart): Cart {
        this.get(newCart.id)
        return this.jpa.save(newCart)
    }

    override fun delete(id: String): Cart {
        val cart = this.get(id)
        this.jpa.delete(cart)
        return cart
    }

    override fun valid(id: String): Boolean {
        val cart = this.get(id)

        cart.items.clear()
        this.jpa.save(cart)
        return true
    }
}

interface CartJpaRepository : JpaRepository<Cart, String>
