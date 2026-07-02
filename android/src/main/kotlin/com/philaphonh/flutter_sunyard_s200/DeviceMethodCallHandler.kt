package com.philaphonh.flutter_sunyard_s200

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class DeviceMethodCallHandler(deviceModule: DeviceModule): MethodCallHandler {
    private lateinit var deviceModule: DeviceModule;

    init {
        this.deviceModule = deviceModule
    }
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        android.util.Log.d("S200_DeviceHandler", "Method called: ${call.method}")
        when(call.method) {
            deviceModule.isAvailableString -> {
                val available = deviceModule.isAvailable()
                android.util.Log.d("S200_DeviceHandler", "isAvailable result: $available")
                result.success(available)
            }
            else -> {
                android.util.Log.w("S200_DeviceHandler", "Method not implemented: ${call.method}")
                result.notImplemented()
            }
        }
    }
}