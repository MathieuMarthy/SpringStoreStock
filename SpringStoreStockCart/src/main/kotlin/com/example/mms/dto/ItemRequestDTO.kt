package com.example.mms.dto

import jakarta.validation.constraints.Min
import kotlinx.serialization.Serializable

@Serializable
data class ItemRequestDTO(
    val itemId: Int,
    @field:Min(0) val quantity: Int
)
