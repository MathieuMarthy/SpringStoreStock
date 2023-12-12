package com.example.mms.models

import jakarta.persistence.*

@Entity
@Table(name = "carts")
data class Cart(
    @Id
    val id: String,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "id")
    val items: MutableList<ItemInCart> = mutableListOf()
)
