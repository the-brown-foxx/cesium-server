package com.thebrownfoxx

import com.thebrownfoxx.totp.logic.decrypt
import com.thebrownfoxx.totp.logic.encrypt
import com.thebrownfoxx.totp.logic.toBase32
import com.thebrownfoxx.totp.logic.toPlainText
import kotlin.test.Test

class EncryptionTest {
    @Test
    fun encryptDecryptTest() {
        val originalText = "The brown fox raw-dogged the sussy dog."
        val decryptedText = originalText.toBase32().encrypt().decrypt().toPlainText()
        println(decryptedText)
        assert(originalText == decryptedText)
    }

    @Test
    fun base32Test() {
        val originalText = "The brown fox raw-dogged the sussy dog."
        val decodedText = originalText.toBase32().toPlainText()
        println(decodedText)
        assert(originalText == decodedText)
    }
}