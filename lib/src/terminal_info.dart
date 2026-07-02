import 'package:flutter/services.dart';

/// A class for retriving terminal information.
class TerminalInfo {
  static const MethodChannel _methodChannel = MethodChannel(_methodChannelName);

  static const String _methodChannelName = "terminal_info";

  static const String _getSerialNumberMethodString = "serialNumber";

  /// Terminal serial number.
  static Future<String> get serialNumber async {
    try {
      final result =
          await _methodChannel.invokeMethod(_getSerialNumberMethodString);
      if (result != null) {
        return result;
      }

      return "";
    } catch (e) {
      rethrow;
    }
  }
}
