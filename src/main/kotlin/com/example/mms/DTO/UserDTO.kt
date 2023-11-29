package com.example.mms.DTO

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "user")
data class UserDTO(
    @field:NotBlank(message = "First Name is mandatory")
    val firstName : String,
    @field:Size(min = 2, max = 30)
    val lastName : String,
    @field:Min(15, message = "Age should not be less than 15")
    var age : Int,
    @Id
    @field:Email(message = "Email should be valid")
    val email : String
) {
}