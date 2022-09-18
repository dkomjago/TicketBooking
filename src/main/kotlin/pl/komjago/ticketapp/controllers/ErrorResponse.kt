package pl.komjago.ticketapp.controllers

import com.fasterxml.jackson.annotation.JsonInclude

data class ErrorResponse<T>(
    val message: String,
    val status: Int,
    @JsonInclude(JsonInclude.Include.NON_NULL) val payload: T? = null
)