package com.thebrownfoxx.totp.logic

import com.thebrownfoxx.models.totp.Base32
import com.thebrownfoxx.models.totp.EncryptedBase32
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

private const val ALGORITHM = "AES/ECB/PKCS5Padding"
private const val KEY_ALGORITHM = "AES"

private val random = Random(AES_KEY_SEED)
private val aesKey = random.nextBytes(16)

private fun String.encrypt(): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val secretKeySpec = SecretKeySpec(aesKey, KEY_ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val encryptedBytes = cipher.doFinal(toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

private fun String.decrypt(): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    val secretKeySpec = SecretKeySpec(aesKey, KEY_ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val encryptedBytes = Base64.getDecoder().decode(this)
    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes)
}

fun Base32.encrypt() = EncryptedBase32(value.encrypt())

fun EncryptedBase32.decrypt() = Base32(value.decrypt())