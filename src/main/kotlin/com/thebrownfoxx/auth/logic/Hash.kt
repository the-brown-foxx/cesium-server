package com.thebrownfoxx.auth.logic

import com.thebrownfoxx.auth.models.Base64
import com.thebrownfoxx.auth.models.Hash
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom

fun generateSalt(): Base64 {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)
    return salt.toString().toBase64()
}

fun String.hash(salt: Base64 = generateSalt()): Hash {
    val md = MessageDigest.getInstance("SHA-512")
    return Hash(
        value = BigInteger(1, md.digest(toByteArray() + salt.toPlainText().toByteArray())).toString().toBase64(),
        salt = salt,
    )
}

infix fun String.matches(hash: Hash) = hash(hash.salt) == hash

infix fun String.doesNotMatch(hash: Hash) = !matches(hash)