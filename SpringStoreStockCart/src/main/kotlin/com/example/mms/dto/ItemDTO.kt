package com.example.mms.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDTO(
        val name: String,
        val price: Double,
        val stock: Int
)
