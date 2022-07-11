package com.hardik.paymentayakya

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hardik.paymentayakya.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestSMSReadPermission()
        requestNotificationReadPermission()
    }

    /**
     * check if notification read permission is granted or not
     */
    private fun requestNotificationReadPermission() {
        binding.txtAppMessage.isVisible = true
        val permission = Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
        val permissionResult = checkCallingOrSelfPermission(permission);
        val isPermissionGranted = permissionResult != PackageManager.PERMISSION_DENIED
        if (isPermissionGranted) return
        binding.txtAppMessage.isVisible = false
        val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        startActivity(intent)
    }

    /**
     * check if read SMS permission is granted or not
     */
    private fun requestSMSReadPermission() {
        val smsPermission: String = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, smsPermission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = smsPermission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                    111
                )
            } else {
                ActivityCompat.requestPermissions(this, permissionList, 1)
            }
        }
    }
}