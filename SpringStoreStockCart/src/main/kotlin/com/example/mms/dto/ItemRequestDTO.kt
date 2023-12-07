package com.example.mms.dto

import jakarta.validation.constraints.Min

data class ItemRequestDTO(
    val itemId: Int,
    @field:Min(0) val quantity: Int
)
