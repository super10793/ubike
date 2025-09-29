package com.demo.ubike.network

sealed class ApiException(message: String) : Exception(message) {
    abstract val errorMessage: String

    data class BadNetwork(override val errorMessage: String = "Bad Network") : ApiException(errorMessage)

    data class Timeout(override val errorMessage: String = "Timeout") : ApiException(errorMessage)

    data class Http(val code: Int, override val errorMessage: String = "Http error") : ApiException(errorMessage)

    data class InvalidBody(override val errorMessage: String = "Response body is null", val errorType: String? = null) : ApiException(errorMessage)

    data class InvalidToken(override val errorMessage: String = "Invalid token"): ApiException(errorMessage)

    data class Unknown(override val errorMessage: String = "Unknown error") : ApiException(errorMessage)
}