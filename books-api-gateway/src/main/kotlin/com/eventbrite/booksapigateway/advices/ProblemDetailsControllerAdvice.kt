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
    override fun prepare(throwable: Throwable, status: StatusType, type: URI): ProblemBuilder {
        if (throwable is StatusRuntimeException) {
            var builder = Problem.builder()
                .withTitle(Status.NOT_FOUND.reasonPhrase)
                .withStatus(Status.NOT_FOUND)
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