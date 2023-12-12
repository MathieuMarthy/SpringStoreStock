package com.example.mms.errors

open class ItemException(message: Any) : Exception(message.toString())

class ItemNotEnoughStockException(message: Any) : ItemException(message)

class ItemNotFoundException(message: Any) : ItemException(message)

class ItemAlreadyExistsException(message: Any) : ItemException(message)
