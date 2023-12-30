package com.thebrownfoxx.totp.models

import com.thebrownfoxx.totp.AES_KEY_SEED
import kotlinx.serialization.Serializable
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

@Serializable
data class EncryptedBase32(val value: String)