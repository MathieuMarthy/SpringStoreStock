package com.example.mms.errors

open class CartException(message: Any) : Exception(message.toString())

class CartNotFoundException(message: Any) : CartException(message)

class CartAlreadyExistsException(message: Any) : CartException(message)
