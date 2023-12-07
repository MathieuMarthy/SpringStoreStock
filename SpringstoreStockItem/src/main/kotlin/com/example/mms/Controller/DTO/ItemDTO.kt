package com.example.mms.Controller.DTO

import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat

data class ItemDTO(
    @field:Size(min = 2, max = 30) val name: String,
    val price: Double,
    val stock: Int,
    @field:DateTimeFormat val dateLastUpdate: String,
){

}