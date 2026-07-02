package com.philaphonh.flutter_sunyard_i80

import com.socsi.smartposapi.DeviceMaster

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

/** FlutterSunyardI80Plugin */
class FlutterSunyardI80Plugin: FlutterPlugin {
  /** Method channel for [PrinterModule] */
  private var printerChannel: MethodChannel? = null

  /** Method channel for [TerminalInfoModule] */
  private var terminalInfoChannel: MethodChannel? = null

  /** Method channel for [DeviceModule] */
  private var deviceChannel: MethodChannel? = null

  /** Method channel string of [printerChannel] */
  private val printerChannelName: String = "printer"

  /** Method channel string of [terminalInfoChannel] */
  private val terminalInfoChannelName: String = "terminal_info"

  /** Method channel string of [deviceChannel] */
  private val deviceChannelName: String = "device"

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val deviceModule = DeviceModule()
    deviceChannel = MethodChannel(flutterPluginBinding.binaryMessenger, deviceChannelName)
    deviceChannel?.setMethodCallHandler(DeviceMethodCallHandler(deviceModule))

    val isDeviceAvailable = deviceModule.isAvailable()
    if (isDeviceAvailable) {
      DeviceMaster.getInstance().init(flutterPluginBinding.applicationContext)

      printerChannel = MethodChannel(flutterPluginBinding.binaryMessenger, printerChannelName)
      printerChannel?.setMethodCallHandler(PrinterMethodCallHandler(PrinterModule()))

      terminalInfoChannel = MethodChannel(flutterPluginBinding.binaryMessenger, terminalInfoChannelName)
      terminalInfoChannel?.setMethodCallHandler(TerminalInfoMethodCallHandler(TerminalInfoModule()))
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    printerChannel?.setMethodCallHandler(null)
    terminalInfoChannel?.setMethodCallHandler(null)
  }
}
