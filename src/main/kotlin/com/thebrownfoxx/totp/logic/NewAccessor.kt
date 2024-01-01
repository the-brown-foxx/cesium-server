package com.thebrownfoxx.totp.logic

import com.thebrownfoxx.models.totp.UnsavedAccessor

fun newAccessor(name: String) = UnsavedAccessor(
    name = name,
    totpSecret = generateTotpSecret().encrypt(),
)