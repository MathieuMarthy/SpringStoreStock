package com.example.mms.Model

import java.util.*

data class Item(
        val id: Int,
        val name: String,
        val price: Double,
        val stock: Int,
        val dateLastUpdate: Date
)