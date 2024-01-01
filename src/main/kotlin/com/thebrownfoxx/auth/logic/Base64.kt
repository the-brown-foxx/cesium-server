package com.thebrownfoxx.auth.logic

import com.thebrownfoxx.models.auth.Base64
import java.util.Base64.*

fun String.toBase64() = Base64(getEncoder().encodeToString(this.toByteArray()))

fun Base64.toPlainText() = String(getDecoder().decode(this.value))