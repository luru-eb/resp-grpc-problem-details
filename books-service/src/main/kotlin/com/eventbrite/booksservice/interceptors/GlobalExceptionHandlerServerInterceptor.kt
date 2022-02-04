package com.eventbrite.booksservice.interceptors

import io.grpc.*
import com.eventbrite.foundations.exceptions.ResourceNotFoundException as ResourceNotFoundException

//@GrpcGlobalServerInterceptor
class GlobalExceptionHandlerServerInterceptor: ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>?,
        headers: Metadata?,
        next: ServerCallHandler<ReqT, RespT>?
    ): ServerCall.Listener<ReqT> {
        return ExceptionHandlerCallListener(
            next?.startCall(call, headers)!!,
            call,
            headers
        )
    }

    private class ExceptionHandlerCallListener<ReqT,RespT>(
        delegate: ServerCall.Listener<ReqT>,
        val serverCall: ServerCall<ReqT, RespT>?,
        val headers: Metadata?
    ) : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
        override fun onHalfClose() {
            try {
                super.onHalfClose()
            }
            catch (e: RuntimeException){
                when (e) {
                    is ResourceNotFoundException -> {
                        val metadata = Metadata()
                        e.metadata.forEach {
                            metadata.put(
                                Metadata.Key.of(it.key, Metadata.ASCII_STRING_MARSHALLER),
                                it.value
                            )
                        }
                        val statusRuntimeException = Status.NOT_FOUND
                            .withDescription(e.message)
                            .asRuntimeException(metadata)

                        serverCall?.close(Status.fromThrowable(statusRuntimeException), metadata)
                    }
                    else -> {
                        serverCall?.close(Status.UNKNOWN, headers)
                    }
                }
            }
        } 
    }
}