package com.example.mms.controllerAdvice

import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CartControllerAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(CartNotFoundException::class)
    fun handleConstraintViolationException(e: CartNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(404).body("")
    }

    @ExceptionHandler(CartAlreadyExistsException::class)
    fun handleConstraintViolationException(e: CartAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.status(409).body(e.message)
    }

    override fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException,
                                              headers: HttpHeaders,
                                              status: HttpStatusCode,
                                              request: WebRequest
    )
            : ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body("argument not valid")
    }
}
