package com.thebrownfoxx.totp.logic

import com.thebrownfoxx.models.totp.Base32

fun generateTotpSecret(): Base32 {
    val allowedCharacters = ('A'..'Z') + (2..7)
    return Base32(Array(16) { allowedCharacters.random() }.joinToString(""))
}