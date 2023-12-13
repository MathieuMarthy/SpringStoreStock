package com.example.mms.Controller

import com.example.mms.Controller.DTO.UserDTO
import com.example.mms.Errors.UserNotFoundException
import com.example.mms.Repository.UserEntity
import com.example.mms.Repository.UserRepository
import com.example.mms.Repository.asEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(summary = "Create a new user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User created",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]),
        ApiResponse(responseCode = "409", description = "User already exists",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PostMapping("api/users")
    fun create(@RequestBody @Valid user : UserDTO): ResponseEntity<UserEntity> =
        userRepository.create(user.asUser()).fold(
            { s -> ResponseEntity.status(HttpStatus.CREATED).body(s.asEntity())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @Operation(summary = "Get all newsletter followers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the users",
            content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = UserEntity::class)))]),
        ApiResponse(responseCode = "404", description = "No users found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @GetMapping("api/users/newsletter")
    fun getAllNewsletter(): ResponseEntity<List<UserEntity>> =
        userRepository.getAllNewsletterFollowers()
            .map { it.asEntity() }
            .let { ResponseEntity.ok(it) }

    @Operation(summary = "Get all users")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the users",
            content = [Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = UserEntity::class)))]),
        ApiResponse(responseCode = "404", description = "No users found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @GetMapping("api/users")
    fun getAllUsers(): ResponseEntity<List<UserEntity>> =
        userRepository.getAll()
            .map { it.asEntity() }
            .let { ResponseEntity.ok(it) }

    @Operation(summary = "Get a user by its email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the user",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]),
        ApiResponse(responseCode = "404", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @GetMapping("api/users/{email}")
    fun getOneUser(@PathVariable @Valid @Email email : String): ResponseEntity<UserEntity> {
        val user = userRepository.get(email)
        return if (user != null) ResponseEntity.ok(user.asEntity())
        else throw UserNotFoundException(email)
    }

    @Operation(summary = "Update a user by its email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User updated",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]),
        ApiResponse(responseCode = "404", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PutMapping("api/users/{email}")
    fun updateUser(@PathVariable @Valid @Email email: String, @RequestBody user : UserDTO): ResponseEntity<Any> =
        if ( email != user.email) ResponseEntity.badRequest().body("Email in path and body are not the same")
        else
        userRepository.update(user.asUser()).fold(
            { s -> ResponseEntity.ok(s.asEntity())},
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @Operation(summary = "Delete a user by its email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "User deleted",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]),
        ApiResponse(responseCode = "404", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("api/users/{email}")
    fun deleteUser(@PathVariable @Valid @Email email : String): ResponseEntity<Any> =
        userRepository.delete(email)?.let { ResponseEntity.noContent().build() }
        ?: throw UserNotFoundException(email)

    @Operation(summary = "Update the last command date of a user by its email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "User updated",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]),
        ApiResponse(responseCode = "404", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @PutMapping("api/users/{email}/updateLastCommandDate")
    fun updateLastCommandDate(@PathVariable @Valid @Email email: String): ResponseEntity<Any> =
        userRepository.updateLastCommandDate(email).let { ResponseEntity.noContent().build() }
}
