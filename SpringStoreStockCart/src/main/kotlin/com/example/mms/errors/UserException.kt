package com.example.mms.errors


open class UserException(message: Any) : Exception(message.toString())

class UserNotFoundException(message: Any) : UserException(message)

class UserAlreadyExistsException(message: Any) : UserException(message)
