package com.example.mms.models

import jakarta.persistence.*

@Entity
@Table(name = "itemsInCart")
class ItemInCart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val itemId: Int,
    var quantity: Int,
)
