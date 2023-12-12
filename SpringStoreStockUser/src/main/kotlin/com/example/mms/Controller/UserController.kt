package com.example.mms.Controller

import com.example.mms.Controller.DTO.UserDTO
import com.example.mms.Controller.DTO.asDTO
import com.example.mms.Errors.UserNotFoundException
import com.example.mms.Repository.UserRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userRepository: UserRepository) {

    @PostMapping("api/users")
    fun create(@RequestBody @Valid user : UserDTO): ResponseEntity<UserDTO> =
        userRepository.create(user.asUser()).fold(
            { s -> ResponseEntity.status(HttpStatus.CREATED).body(s.asDTO())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @GetMapping("api/users/newsletter")
    fun getAllNewsletter(): ResponseEntity<List<UserDTO>> =
        userRepository.getAllNewsletterFollowers()
            .map { it.asDTO() }
            .let { ResponseEntity.ok(it) }

    @GetMapping("api/users")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> =
        userRepository.getAll()
            .map { it.asDTO() }
            .let { ResponseEntity.ok(it) }

    @GetMapping("api/users/{email}")
    fun getOneUser(@PathVariable @Valid email : String): ResponseEntity<UserDTO> {
        val user = userRepository.get(email)
        return if (user != null) ResponseEntity.ok(user.asDTO())
        else throw UserNotFoundException("User not found for the email $email")
    }

    @PutMapping("api/users/{email}")
    fun updateUser(@Valid email: String, @RequestBody user : UserDTO): ResponseEntity<Any> =
        if ( email != user.email) ResponseEntity.badRequest().body("Email in path and body are not the same")
        else
        userRepository.update(user.asUser()).fold(
            { s -> ResponseEntity.ok(s.asDTO())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @DeleteMapping("api/users/{email}")
    fun deleteUser(@PathVariable @Valid email : String): ResponseEntity<Any> =
        userRepository.delete(email)?.let { ResponseEntity.noContent().build() }
        ?: throw UserNotFoundException("User not found for the email $email")
    
    @PutMapping("api/users/{email}/updateLastCommandDate")
    fun updateLastCommandDate(@PathVariable @Valid email: String): ResponseEntity<Any> =
        userRepository.updateLastCommandDate(email).let { ResponseEntity.noContent().build() }
}
