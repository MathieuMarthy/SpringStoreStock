package com.example.mms.Models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


@Entity
@Table(name = "items")
data class Item(
        @Id
        val id : Int,
        val name : String,
        val price : Double,
        val quantity : Int,
        val dateLastUpdate : Date
)
