package com.eventbrite.booksapigateway.controllers

import com.eventbrite.booksapigateway.services.BooksService
import com.eventbrite.booksapigateway.dto.Book
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController(private val service: BooksService) {

    @GetMapping("/books/{isbn}")
    fun getBook(@PathVariable isbn: String): ResponseEntity<Book> {
        val response = service.getBook(isbn)
        return ResponseEntity(
            Book(
                response.isbn,
                response.title,
                response.author,
                response.page
            ),
            HttpStatus.OK
        )
    }
}