package com.philaphonh.flutter_sunyard_s200

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class TerminalInfoMethodCallHandler(terminalInfoModule: TerminalInfoModule): MethodCallHandler {
    /** An instance of [TerminalInfoModule] */
    private lateinit var terminalInfoModule: TerminalInfoModule;

    init {
        this.terminalInfoModule = terminalInfoModule
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        android.util.Log.d("S200_TerminalHandler", "Method called: ${call.method}")
        when (call.method) {
            terminalInfoModule.snGetterString -> {
                android.util.Log.d("S200_TerminalHandler", "Serial number: ${terminalInfoModule.serialNumber}")
                result.success(terminalInfoModule.serialNumber)
            }
            else -> {
                android.util.Log.w("S200_TerminalHandler", "Method not implemented: ${call.method}")
                result.notImplemented()
            }
        }
    }
}