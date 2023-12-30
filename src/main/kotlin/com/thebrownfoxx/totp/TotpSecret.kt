package com.thebrownfoxx.totp

import com.thebrownfoxx.totp.logic.toBase32
import com.thebrownfoxx.totp.models.Base32

fun generateTotpSecret(): Base32 {
    val allowedCharacters = ('a'..'z') + ('A'..'Z') + (0..9)
    val plainText = Array(10) { allowedCharacters.random() }.toString()
    return plainText.toBase32()
}