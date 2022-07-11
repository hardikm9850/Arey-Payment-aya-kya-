package com.hardik.paymentayakya

import android.app.Notification
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NotificationInterceptorService : NotificationListenerService() {
    private val TAG = NotificationInterceptorService::class.simpleName
    private val appOfInterest = listOf(
        "com.google.android.apps.nbu.paisa.user",//gpay
        "com.phonepe.app", // phomepe consumer
        "com.phonepe.app.business" //phonepe business
    )

    override fun onBind(mIntent: Intent?): IBinder? {
        val mIBinder = super.onBind(mIntent)
        Log.i(TAG, "onBind")
        return mIBinder
    }


    override fun onUnbind(mIntent: Intent?): Boolean {
        val mOnUnbind = super.onUnbind(mIntent)
        Log.i(TAG, "onUnbind")
        try {
        } catch (e: Exception) {
            Log.e(TAG, "Error during unbind", e)
        }
        return mOnUnbind
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        handleNotification(sbn)
    }

    private fun handleNotification(statusBarNotification: StatusBarNotification?) {
        checkNotNull(statusBarNotification)
        val packageName = statusBarNotification.packageName
        Log.d(TAG, " $packageName")
        if(appOfInterest.contains(packageName).not()) return
        val text = statusBarNotification.notification.tickerText?.toString() ?: ""
        val notificationText =
            statusBarNotification.notification.extras.getCharSequence(Notification.EXTRA_TEXT)
                .toString();
        val notificationTitle =
            statusBarNotification.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
                .toString()
        Log.d(TAG, " title $notificationTitle") // check for amount
        Log.d(TAG, " text $notificationText")

        parseMessage(notificationTitle)
    }

    private fun parseMessage(receivedMessage: String) {
        val amount = receivedMessage.getAmountFromText()
        if(amount.isEmpty()) return
        with(applicationContext) {
            announceText(getString(R.string.amount_received, amount))
        }
    }
}