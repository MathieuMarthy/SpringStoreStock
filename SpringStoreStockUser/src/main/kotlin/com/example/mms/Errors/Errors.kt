package com.example.mms.Errors

open class UserException(override val message: String?) : Exception(message)

class UserNotFoundException(override val message: String?) : UserException(message)

class UserAlreadyExistsException(override val message: String?) : UserException(message)
