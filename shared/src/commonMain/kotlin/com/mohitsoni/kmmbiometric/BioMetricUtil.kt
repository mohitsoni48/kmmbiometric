package com.mohitsoni.kmmbiometric

interface BioMetricUtil {

    fun canAuthenticate(): Boolean
    suspend fun generatePublicKey(): String?
    fun getPublicKey(): String?
    fun isValidCrypto(): Boolean
    suspend fun authenticate(): AuthenticationResult
    fun signUserId(ucc: String): String
    fun isBiometricSet(): Boolean
}

sealed class AuthenticationResult {
    data object Success: AuthenticationResult()
    data object Failed: AuthenticationResult()
    data object AttemptExhausted: AuthenticationResult()
    data object NegativeButtonClick: AuthenticationResult()
    data class Error(val error: String): AuthenticationResult()
}