package com.philaphonh.flutter_sunyard_i80

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
        when (call.method) {
            terminalInfoModule.snGetterString -> {
                result.success(terminalInfoModule.serialNumber)
            }
            else -> result.notImplemented()
        }
    }
}