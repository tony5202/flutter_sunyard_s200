import 'package:flutter/services.dart';

/// A class for device status
class Device {
  static const MethodChannel _methodChannel = MethodChannel(_methodChannelName);
  
  static const String _methodChannelName = "device";
  
  static const String _isAvailableMethodString = "isAvailable";

  /// Whether the device is Sunyard i80 or not
  static Future<bool> isAvailable() async {
    try {
      final result = await _methodChannel.invokeMethod(_isAvailableMethodString);

      return result;
    } catch (e) {
      return false;
    }
  }
}