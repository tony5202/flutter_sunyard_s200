package com.philaphonh.flutter_sunyard_i80

/** A module for device status */
class DeviceModule {
    /** Method string of [isAvailable] */
    val isAvailableString: String = "isAvailable";

    /** Check if this device is the Sunyard i80 or not */
    fun isAvailable(): Boolean {
        return System.getProperty("http.agent").contains("i80");
    }
}