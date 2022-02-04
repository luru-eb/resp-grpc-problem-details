package com.eventbrite.booksservice.advices

import com.eventbrite.foundations.exceptions.ResourceNotFoundException
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import net.devh.boot.grpc.server.advice.GrpcAdvice
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler

@GrpcAdvice
class ExceptionHandlerGrpcAdvice {
    @GrpcExceptionHandler
    fun handleResourceNotFoundException(e: ResourceNotFoundException) : StatusRuntimeException {
        val metadata = Metadata()
        e.metadata.forEach {
            metadata.put(
                Metadata.Key.of(it.key, Metadata.ASCII_STRING_MARSHALLER),
                it.value
            )
        }
        return Status.NOT_FOUND
            .withDescription(e.message)
            .asRuntimeException(metadata)
    }
}