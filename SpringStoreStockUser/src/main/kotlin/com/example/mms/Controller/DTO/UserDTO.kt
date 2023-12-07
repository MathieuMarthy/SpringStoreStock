package com.example.mms.Controller.DTO

import com.example.mms.Model.User
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jdk.jfr.BooleanFlag
import org.springframework.format.annotation.DateTimeFormat
import java.util.*


data class UserDTO(
    @field:Email val email: String,
    @field:Size(min = 1, max = 30) val firstName: String,
    @field:Size(min = 1, max = 30) val lastName: String,
    @field:Size(min = 1, max = 30) val address: String,
    @field:BooleanFlag val followingNewsletter: Boolean,
    @field:DateTimeFormat val lastCommand: Date,
    ){
    fun asUser() = User(email, firstName, lastName, address,followingNewsletter,lastCommand)
}
fun User.asDTO() = UserDTO(this.email, this.firstName, this.lastName, this.address, this.followingNewsletter, this.lastCommand )
