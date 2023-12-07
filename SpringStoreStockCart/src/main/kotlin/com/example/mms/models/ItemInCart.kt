package com.example.mms.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "itemsInCart")
class ItemInCart(
    @Id
    val itemId: Int,
    var quantity: Int,
)
