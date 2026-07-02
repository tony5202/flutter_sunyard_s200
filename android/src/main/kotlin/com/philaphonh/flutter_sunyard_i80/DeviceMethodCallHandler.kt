package com.philaphonh.flutter_sunyard_i80

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class DeviceMethodCallHandler(deviceModule: DeviceModule): MethodCallHandler {
    private lateinit var deviceModule: DeviceModule;

    init {
        this.deviceModule = deviceModule
    }
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when(call.method) {
            deviceModule.isAvailableString -> {
                result.success(deviceModule.isAvailable())
            }
            else -> result.notImplemented()
        }
    }
}