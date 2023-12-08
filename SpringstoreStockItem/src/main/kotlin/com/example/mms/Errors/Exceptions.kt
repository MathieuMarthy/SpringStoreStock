
open class ItemException(message : Any) : Exception(message.toString())

class ItemNotFoundException(message: Any): ItemException(message)

