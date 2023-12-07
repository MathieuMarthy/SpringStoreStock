package com.example.mms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MmsApplication

fun main(args: Array<String>) {
    runApplication<MmsApplication>(*args)
}
