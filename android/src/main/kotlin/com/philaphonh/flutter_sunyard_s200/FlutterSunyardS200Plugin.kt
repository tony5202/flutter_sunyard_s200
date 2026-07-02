package com.philaphonh.flutter_sunyard_s200

import com.socsi.smartposapi.DeviceMaster

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

/** FlutterSunyardS200Plugin */
class FlutterSunyardS200Plugin: FlutterPlugin {
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
    println("S200_Plugin: === onAttachedToEngine START ===")
    android.util.Log.d("S200_Plugin", "=== onAttachedToEngine START ===")

    val deviceModule = DeviceModule()
    deviceChannel = MethodChannel(flutterPluginBinding.binaryMessenger, deviceChannelName)
    deviceChannel?.setMethodCallHandler(DeviceMethodCallHandler(deviceModule))
    println("S200_Plugin: DeviceChannel registered: $deviceChannelName")
    android.util.Log.d("S200_Plugin", "DeviceChannel registered: $deviceChannelName")

    val isDeviceAvailable = deviceModule.isAvailable()
    println("S200_Plugin: isDeviceAvailable: $isDeviceAvailable")
    android.util.Log.d("S200_Plugin", "isDeviceAvailable: $isDeviceAvailable")

    if (isDeviceAvailable) {
      println("S200_Plugin: Device is available - initializing SDK and registering channels")
      android.util.Log.d("S200_Plugin", "Device is available - initializing SDK and registering channels")
      DeviceMaster.getInstance().init(flutterPluginBinding.applicationContext)

      printerChannel = MethodChannel(flutterPluginBinding.binaryMessenger, printerChannelName)
      printerChannel?.setMethodCallHandler(PrinterMethodCallHandler(PrinterModule()))
      println("S200_Plugin: PrinterChannel registered: $printerChannelName")
      android.util.Log.d("S200_Plugin", "PrinterChannel registered: $printerChannelName")

      terminalInfoChannel = MethodChannel(flutterPluginBinding.binaryMessenger, terminalInfoChannelName)
      terminalInfoChannel?.setMethodCallHandler(TerminalInfoMethodCallHandler(TerminalInfoModule()))
      println("S200_Plugin: TerminalInfoChannel registered: $terminalInfoChannelName")
      android.util.Log.d("S200_Plugin", "TerminalInfoChannel registered: $terminalInfoChannelName")
    } else {
      println("S200_Plugin: Device NOT available - channels NOT registered")
      android.util.Log.e("S200_Plugin", "Device NOT available - channels NOT registered")
    }

    println("S200_Plugin: === onAttachedToEngine END ===")
    android.util.Log.d("S200_Plugin", "=== onAttachedToEngine END ===")
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    printerChannel?.setMethodCallHandler(null)
    terminalInfoChannel?.setMethodCallHandler(null)
  }
}
