package com.eventbrite.booksservice.repositories

import com.eventbrite.books.Book
import com.eventbrite.books.BookType
import com.eventbrite.foundations.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Repository

@Repository
class BookRepository {
    private val books: List<Book> = listOf(
        Book.newBuilder()
            .setISBN("0-7645-2641-3")
            .setAuthor("Uncle Bob")
            .setTitle("Clean Code")
            .setPage(400)
            .setBookType(BookType.BOOK)
            .build()
    )

    fun getBook(isbn: String) : Book {
        val book = books.firstOrNull { it.isbn.equals(isbn) }

        return book ?: throw ResourceNotFoundException("Book not found", mapOf("eb_isbn" to isbn, "eb_message" to "Book not found"))
    }
}