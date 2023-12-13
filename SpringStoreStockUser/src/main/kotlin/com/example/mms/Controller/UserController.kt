package com.example.mms.Controller

import com.example.mms.Controller.DTO.UserDTO
import com.example.mms.Controller.DTO.UserUpdateDTO
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
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class UserController(val userRepository: UserRepository) {

    @Operation(
        summary = "Create a new user",
        description = "If the user already exists, it will return a 409 error",
        tags = ["Administration"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "User created",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]
            ),
            ApiResponse(
                responseCode = "409", description = "User already exists",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @PostMapping("api/users")
    fun create(@RequestBody @Valid user: UserDTO): ResponseEntity<UserEntity> =
        userRepository.create(user.asUser()).fold(
            { s -> ResponseEntity.status(HttpStatus.CREATED).body(s.asEntity()) },
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )

    @Operation(
        summary = "Get all newsletter followers",
        description = "Get all users that follow the newsletter",
        tags = ["Métier"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the users",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = UserEntity::class))
                )]
            ),
            ApiResponse(
                responseCode = "404", description = "No users found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @GetMapping("api/users/newsletter")
    fun getAllNewsletter(): ResponseEntity<List<UserEntity>> =
        userRepository.getAllNewsletterFollowers()
            .map { it.asEntity() }
            .let { ResponseEntity.ok(it) }

    @Operation(summary = "Get all users", description = "Get all users", tags = ["Métier"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the users",
                content = [Content(
                    mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = UserEntity::class))
                )]
            ),
            ApiResponse(
                responseCode = "404", description = "No users found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @GetMapping("api/users")
    fun getAllUsers(): ResponseEntity<List<UserEntity>> =
        userRepository.getAll()
            .map { it.asEntity() }
            .let { ResponseEntity.ok(it) }

    @Operation(summary = "Get a user by his email", description = "Get a user by its email", tags = ["Métier"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the user",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @GetMapping("api/users/{email}")
    fun getOneUser(@PathVariable @Valid @Email email: String): ResponseEntity<UserEntity> {
        val user = userRepository.get(email)
        return if (user != null) ResponseEntity.ok(user.asEntity())
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @Operation(
        summary = "Update a user by its email",
        description = "Update a user by its email",
        tags = ["Administration"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "User updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @PutMapping("api/users/{email}")
    fun updateUser(@PathVariable @Valid @Email email: String, @RequestBody user: UserUpdateDTO): ResponseEntity<Any> =
        userRepository.update(email, user.asUser()).fold(
            { s -> ResponseEntity.ok(s.asEntity()) },
            { e -> ResponseEntity.status(HttpStatus.CONFLICT).build() }
        )


    @Operation(
        summary = "Delete a user by its email",
        description = "Delete a user by its email",
        tags = ["Administration"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204", description = "User deleted",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @DeleteMapping("api/users/{email}")
    fun deleteUser(@PathVariable @Valid @Email email: String): ResponseEntity<Any> =
        userRepository.delete(email)?.let { ResponseEntity.noContent().build() }
            ?: throw UserNotFoundException(email)

    @Operation(
        summary = "Update the last command date of a user by its email",
        description = "Update the last command date of a user by its email",
        tags = ["Métier"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204", description = "User updated",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserEntity::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "User not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]
            )
        ]
    )
    @PutMapping("api/users/{email}/updateLastCommandDate")
    fun updateLastCommandDate(@PathVariable @Valid @Email email: String): ResponseEntity<Any> =
        userRepository.updateLastCommandDate(email).let { ResponseEntity.noContent().build() }
}
