package com.mohitsoni.kmmbiometric

import android.app.Activity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.Base64
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BiometricUtilAndroidImpl(
    private val activity: Activity,
    private val cipherUtil: ICipherUtil
) : BioMetricUtil {

    private val executor = ContextCompat.getMainExecutor(activity)
    private var promptInfo: PromptInfo? = null
    private var biometricPrompt: BiometricPrompt? = null

    override suspend fun setAndReturnPublicKey(): String? {
        val authenticateResult = authenticate()
        return when (authenticateResult) {
            is AuthenticationResult.Success -> generatePublicKey()
            else -> null
        }
    }

    override fun canAuthenticate(): Boolean {
        return BiometricManager.from(activity).canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }

    override suspend fun generatePublicKey(): String? {
        return cipherUtil.generateKeyPair().public?.encoded?.toBase64Encoded()?.toPemFormat()?.toBase64Encoded()
    }

    override fun getPublicKey(): String? {
        return cipherUtil.getPublicKey().encoded?.toBase64Encoded()?.toPemFormat()?.toBase64Encoded()
    }

    override fun isValidCrypto(): Boolean {
        return try {
            cipherUtil.getCrypto()
            true
        } catch (e: Exception){
            false
        }
    }

    override suspend fun authenticate(): AuthenticationResult = suspendCoroutine { continuation ->

        biometricPrompt = BiometricPrompt(activity as FragmentActivity, executor, object :
            AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                when (errorCode) {
                    BiometricPrompt.ERROR_LOCKOUT, BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> continuation.resume(AuthenticationResult.AttemptExhausted)
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> continuation.resume(AuthenticationResult.NegativeButtonClick)
                    else -> continuation.resume(AuthenticationResult.Error(errString.toString()))
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                continuation.resume(AuthenticationResult.Success)
            }
        })

        promptInfo?.let {
            biometricPrompt?.authenticate(it, cipherUtil.getCrypto())
        }
    }

    override fun signUserId(ucc: String): String {
        cipherUtil.getCrypto().signature?.update(ucc.toByteArray())
        return cipherUtil.getCrypto().signature?.sign()?.toBase64Encoded() ?: ""
    }

    override fun isBiometricSet(): Boolean {
        return !getPublicKey().isNullOrEmpty() && isValidCrypto()
    }

    fun preparePrompt(
        title: String,
        subtitle: String,
        description: String,
    ): BioMetricUtil {
        promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        return this
    }
}

fun ByteArray.toBase64Encoded(): String? {
    return Base64.getEncoder().encodeToString(this)
}

fun String.toBase64Encoded(): String? {
    return Base64.getEncoder().encodeToString(this.toByteArray())
}

private fun String.toPemFormat(): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("-----BEGIN RSA PUBLIC KEY-----").append("\n")
    chunked(64).forEach {
        stringBuilder.append(it).append("\n")
    }
    stringBuilder.append("-----END RSA PUBLIC KEY-----")
    return stringBuilder.toString()
}