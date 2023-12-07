package com.example.mms.Models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "carts")
data class Cart(
        @Id
        val id : Int,
        val userId : Int
)
