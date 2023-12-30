package com.thebrownfoxx

import com.thebrownfoxx.auth.logic.hash
import com.thebrownfoxx.auth.logic.verify
import kotlin.test.Test

class HashTest {
    @Test
    fun hashTest() {
        val originalText = "The brown fox raw-dogged the sussy dog."
        val hash = originalText.hash()
        assert(originalText.verify(hash))
    }

    @Test
    fun saltTest() {
        val originalText = "The brown fox raw-dogged the sussy dog."
        val hash1 = originalText.hash()
        val hash2 = originalText.hash()
        assert(hash1 != hash2)
    }
}