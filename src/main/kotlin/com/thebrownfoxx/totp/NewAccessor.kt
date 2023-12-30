package com.thebrownfoxx.totp

import com.thebrownfoxx.totp.logic.encrypt
import com.thebrownfoxx.totp.models.UnsavedAccessor

fun newAccessor(name: String) = UnsavedAccessor(
    name = name,
    totpSecret = generateTotpSecret().encrypt(),
)