package com.philaphonh.flutter_sunyard_s200

import com.socsi.smartposapi.DeviceMaster

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

/** FlutterSunyardS200Plugin */
class FlutterSunyardS200Plugin: FlutterPlugin {
  init {
    android.util.Log.e("S200_Plugin", "=== PLUGIN CLASS INITIALIZED ===")
  }

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
    try {
      android.util.Log.e("S200_Plugin", "=== onAttachedToEngine START ===")

      // Always register device channel first
      val deviceModule = DeviceModule()
      deviceChannel = MethodChannel(flutterPluginBinding.binaryMessenger, deviceChannelName)
      deviceChannel?.setMethodCallHandler(DeviceMethodCallHandler(deviceModule))
      println("S200_Plugin: DeviceChannel registered: $deviceChannelName")
      android.util.Log.e("S200_Plugin", "DeviceChannel registered: $deviceChannelName")

      val isDeviceAvailable = deviceModule.isAvailable()
      println("S200_Plugin: isDeviceAvailable: $isDeviceAvailable")
      android.util.Log.e("S200_Plugin", "isDeviceAvailable: $isDeviceAvailable")

      // ALWAYS register printer and terminal channels
      // This ensures MissingPluginException doesn't occur even if device detection fails
      // Each module will handle the "not available" case internally
      printerChannel = MethodChannel(flutterPluginBinding.binaryMessenger, printerChannelName)

      // Use service-based approach instead of JAR SDK
      val printerService = PrinterModuleService()
      // Connect to the service
      printerService.connect(flutterPluginBinding.applicationContext)

      printerChannel?.setMethodCallHandler(PrinterServiceMethodCallHandler(printerService))
      println("S200_Plugin: PrinterChannel registered: $printerChannelName (Service-based)")
      android.util.Log.e("S200_Plugin", "PrinterChannel registered: $printerChannelName (Service-based)")

      terminalInfoChannel = MethodChannel(flutterPluginBinding.binaryMessenger, terminalInfoChannelName)
      terminalInfoChannel?.setMethodCallHandler(TerminalInfoMethodCallHandler(TerminalInfoModule()))
      println("S200_Plugin: TerminalInfoChannel registered: $terminalInfoChannelName")
      android.util.Log.e("S200_Plugin", "TerminalInfoChannel registered: $terminalInfoChannelName")

      // Only initialize SDK if device is available
      if (isDeviceAvailable) {
        println("S200_Plugin: Device is available - initializing SDK")
        android.util.Log.e("S200_Plugin", "Device is available - initializing SDK")
        DeviceMaster.getInstance().init(flutterPluginBinding.applicationContext)
      } else {
        println("S200_Plugin: Device NOT available - SDK not initialized, but channels registered")
        android.util.Log.w("S200_Plugin", "Device NOT available - SDK not initialized, but channels registered")
      }

      println("S200_Plugin: === onAttachedToEngine END ===")
      android.util.Log.e("S200_Plugin", "=== onAttachedToEngine END ===")
    } catch (e: Exception) {
      println("S200_Plugin: EXCEPTION in onAttachedToEngine: ${e.message}")
      android.util.Log.e("S200_Plugin", "EXCEPTION in onAttachedToEngine", e)
      e.printStackTrace()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    printerChannel?.setMethodCallHandler(null)
    terminalInfoChannel?.setMethodCallHandler(null)
    deviceChannel?.setMethodCallHandler(null)
  }
}
