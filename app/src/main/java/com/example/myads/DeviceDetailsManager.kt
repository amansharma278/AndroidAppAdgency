package com.example.myads

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class DeviceDetailsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("device_details_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveDeviceDetails(deviceDetails: DeviceDetails) {
        val json = gson.toJson(deviceDetails)
        prefs.edit().putString("device_details", json).apply()
    }

    fun getDeviceDetails(): DeviceDetails? {
        val json = prefs.getString("device_details", null)
        return gson.fromJson(json, DeviceDetails::class.java)
    }

    fun getDeviceId(): Int? {
        return getDeviceDetails()?.id
    }

    fun clearDeviceDetails() {
        prefs.edit().clear().apply()
    }
}
