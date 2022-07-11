package com.hardik.paymentayakya

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*
import java.util.regex.Pattern


var textToSpeech: TextToSpeech? = null

/**
 * retrieves digit from the received message
 */
fun String.getAmountFromText(): String {
    val text = this.replace("₹","Rupees ")
    val numberPatten = "([0-9]*)\\.([0-9]*)"
    val doublePatten = "\\d+(\\.\\d+)?"

    val pattern = Pattern.compile(numberPatten)
    val result = text.split(" ").filter { pattern.matcher(it).matches() }
    if (result.isNotEmpty()) {
        return result[0]
    } else {
        val pattern = Pattern.compile(doublePatten)
        val result = text.split(" ").filter { pattern.matcher(it).matches() }
        if (result.isNotEmpty()) {
            return result[0]
        }
    }
    return ""
}

/**
 * announces the text
 */
fun Context.announceText(message: String) {
    textToSpeech = TextToSpeech(this) { i ->
        if (i != TextToSpeech.ERROR) {
            textToSpeech?.language = Locale.UK
            textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}