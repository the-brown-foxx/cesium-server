package com.thebrownfoxx.totp.logic

import com.thebrownfoxx.totp.models.UnsavedAccessor

fun newAccessor(name: String) = UnsavedAccessor(
    name = name,
    totpSecret = generateTotpSecret().encrypt(),
)