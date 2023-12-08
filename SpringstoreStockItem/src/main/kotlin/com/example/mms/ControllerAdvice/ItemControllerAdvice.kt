import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ItemControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleConstraintViolationException(e: ItemNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(404).body("")
    }

    override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    )
            : ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body("argument not valid")
    }


}