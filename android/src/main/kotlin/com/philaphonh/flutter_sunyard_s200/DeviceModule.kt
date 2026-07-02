package com.philaphonh.flutter_sunyard_s200

/** A module for device status */
class DeviceModule {
    /** Method string of [isAvailable] */
    val isAvailableString: String = "isAvailable";

    /** Check if this device is the Sunyard S200 or not */
    fun isAvailable(): Boolean {
        val model = android.os.Build.MODEL
        android.util.Log.d("S200_Device", "Build.MODEL: $model")
        return model.contains("S200", ignoreCase = true);
    }
}