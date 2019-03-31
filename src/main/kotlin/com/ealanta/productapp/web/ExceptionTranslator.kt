package com.ealanta.productapp.web

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionTranslator {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseBody
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<String> {
        val type = e.requiredType
        e.requiredType
        val message = if (type!!.isEnum) {
            val withComma = type.enumConstants
                    .map { it.toString() }
                    .joinToString(separator= ", ")
            "The parameter ${e.name} must have a value among : $withComma"
        } else {
            "The parameter ${e.name} must be of type ${type.typeName}"
        }
        return ResponseEntity.unprocessableEntity().contentType(MediaType.TEXT_PLAIN).body(message)
    }
}