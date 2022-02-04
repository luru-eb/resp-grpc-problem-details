package com.eventbrite.booksapigateway.services

import com.eventbrite.books.BookRequestId
import com.eventbrite.books.BookServiceGrpc
import io.grpc.ManagedChannel
import org.springframework.stereotype.Service
import com.eventbrite.books.Book

@Service
class BooksService(private val managedChannel: ManagedChannel) {
    fun getBook(isbn: String): Book {
        val request = BookRequestId.newBuilder()
            .setISBN(isbn)
            .build()

        val client = BookServiceGrpc.newBlockingStub(managedChannel)

        return client.getBook(request)
    }
}