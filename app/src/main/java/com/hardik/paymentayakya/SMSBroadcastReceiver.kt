package com.hardik.paymentayakya

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.telephony.SmsMessage
import android.util.Log
import java.util.*


class SMSBroadcastReceiver : BroadcastReceiver() {
    private val TAG = SMSBroadcastReceiver::class.simpleName
    private val BUNDLE_SMS = "pdus"
    private val wordOfInterest = "Credited"

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentExtras = intent?.extras
        Log.d(TAG, "Received")
        if(intentExtras == null) return
        val sms = intentExtras.get(BUNDLE_SMS) as Array<Any>? ?: return
        val messages = arrayOfNulls<SmsMessage>(
            sms.size
        )
        for (i in sms.indices) {
            messages[i] = SmsMessage.createFromPdu(sms[i] as ByteArray)
        }
        if(messages.isEmpty()) return
        val messageBody = messages[0]!!.messageBody
        val sender = messages[0]!!.originatingAddress
        if (messageBody.contains(wordOfInterest).not()) return

        Log.d(TAG, "messageBody $messageBody")
        Log.d(TAG, "sender $sender")

        //get received amount from the text sms
        val amount = messageBody.getAmountFromText()
        if (amount.isEmpty()) return
        context?.announceText(context.getString(R.string.amount_received, amount))
    }
}