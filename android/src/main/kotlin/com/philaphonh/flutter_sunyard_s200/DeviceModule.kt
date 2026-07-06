package com.philaphonh.flutter_sunyard_s200

/** A module for device status */
class DeviceModule {
    init {
        android.util.Log.d("S200_DeviceModule", "=== DEVICE MODULE INITIALIZED ===")
    }

    /** Method string of [isAvailable] */
    val isAvailableString: String = "isAvailable";

    /** Check if this device is the Sunyard S200 or not */
    fun isAvailable(): Boolean {
        val model = android.os.Build.MODEL
        val manufacturer = android.os.Build.MANUFACTURER
        val product = android.os.Build.PRODUCT

        android.util.Log.d("S200_Device", "Build.MODEL: $model")
        android.util.Log.d("S200_Device", "Build.MANUFACTURER: $manufacturer")
        android.util.Log.d("S200_Device", "Build.PRODUCT: $product")

        // Check multiple possible model names for Sunyard S200 devices
        // Common variations: S200, S200-K, sunyard_s200, etc.
        val isSunyardDevice = model.contains("S200", ignoreCase = true) ||
                              model.contains("sunyard", ignoreCase = true) ||
                              manufacturer.contains("sunyard", ignoreCase = true) ||
                              product.contains("s200", ignoreCase = true)

        android.util.Log.d("S200_Device", "isSunyardDevice: $isSunyardDevice")
        return isSunyardDevice
    }
}