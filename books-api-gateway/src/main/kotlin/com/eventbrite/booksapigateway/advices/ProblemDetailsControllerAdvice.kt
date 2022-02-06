package com.eventbrite.booksapigateway.advices

import io.grpc.Metadata
import io.grpc.StatusRuntimeException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.zalando.problem.Problem
import org.zalando.problem.ProblemBuilder
import org.zalando.problem.Status
import org.zalando.problem.StatusType
import org.zalando.problem.spring.web.advice.ProblemHandling
import java.net.URI

@ControllerAdvice
class ProblemDetailsControllerAdvice: ProblemHandling {
    val statusMappings = mapOf(
        io.grpc.Status.Code.NOT_FOUND to Status.NOT_FOUND,
        io.grpc.Status.Code.UNKNOWN to Status.BAD_REQUEST,
        io.grpc.Status.Code.UNIMPLEMENTED to Status.NOT_IMPLEMENTED,
        io.grpc.Status.Code.UNAVAILABLE to Status.SERVICE_UNAVAILABLE,
        io.grpc.Status.Code.UNAUTHENTICATED to Status.UNAUTHORIZED,
        io.grpc.Status.Code.PERMISSION_DENIED to Status.FORBIDDEN,
    ).withDefault { Status.INTERNAL_SERVER_ERROR }

    override fun prepare(throwable: Throwable, status: StatusType, type: URI): ProblemBuilder {
        if (throwable is StatusRuntimeException) {
            val status = statusMappings[throwable.status.code]!!
            var builder = Problem.builder()
                .withTitle(status.reasonPhrase)
                .withStatus(status)
                .withDetail(throwable.message)
            val trailers = throwable.trailers
            for (k in trailers?.keys()!!.filter { it.startsWith("eb_") }){
                val key = Metadata.Key.of(k, Metadata.ASCII_STRING_MARSHALLER)
                builder = builder.with(k, trailers.get(key));
            }

            return builder
        }

        return super.prepare(throwable, status, type)
    }
}