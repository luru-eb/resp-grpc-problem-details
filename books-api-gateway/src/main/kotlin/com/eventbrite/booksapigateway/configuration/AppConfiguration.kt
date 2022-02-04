package com.eventbrite.booksapigateway.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.problem.jackson.ProblemModule
import org.zalando.problem.violations.ConstraintViolationProblemModule

@Configuration
class AppConfiguration {
    @Bean
    fun managedChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext()
            .build()
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModules(
            ProblemModule(),
            ConstraintViolationProblemModule()
        )
    }
}