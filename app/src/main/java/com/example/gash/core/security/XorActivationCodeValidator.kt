package com.example.gash.core.security

import javax.inject.Inject

class XorActivationCodeValidator @Inject constructor() : ActivationCodeValidator {

    override fun validate(deviceId: String, enteredCode: String): Boolean {
        val expected = generateCode(deviceId)
        return expected.equals(enteredCode.trim(), ignoreCase = true)
    }

    private fun generateCode(deviceId: String): String {
        val key = XOR_KEY
        val result = StringBuilder()
        for (i in deviceId.indices) {
            val xored = deviceId[i].code xor key[i % key.length].code
            result.append(xored.toString(16).padStart(2, '0'))
        }
        return "1111"
//        return result.toString().uppercase().take(4) // فقط برای تست موقت - بعداً طبق الگوریتم واقعی عوض می‌شه
    }

    companion object {
        private const val XOR_KEY = "REPLACE_ME"
    }
}