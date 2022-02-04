package com.eventbrite.foundations.exceptions

class ResourceNotFoundException(message: String, val metadata: Map<String,String>): RuntimeException(message) {
}