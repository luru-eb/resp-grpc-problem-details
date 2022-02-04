package com.eventbrite.booksservice.grpc

import com.eventbrite.books.Book
import com.eventbrite.books.BookRequestId
import com.eventbrite.books.BookServiceGrpc
import com.eventbrite.booksservice.repositories.BookRepository
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class BooksGrpcService(private val repository: BookRepository): BookServiceGrpc.BookServiceImplBase() {
    override fun getBook(request: BookRequestId, responseObserver: StreamObserver<Book>) {
        val book = repository.getBook(request.isbn)
        responseObserver.onNext(book)
        responseObserver.onCompleted()
    }
}