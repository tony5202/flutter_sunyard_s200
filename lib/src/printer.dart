import 'package:flutter/services.dart';

import 'printer_align.dart';
import 'printer_font_size.dart';
import 'printer_paper_feed.dart';
import 'printing_resp_code.dart';
import 'utils.dart';

/// A class for utilizing the printer functionality.
class Printer {
  static const MethodChannel _methodChannel = MethodChannel(_methodChannelName);

  static const String _methodChannelName = "printer";

  static const String _isPrinterAvailableMethodString = "isPrinterAvailable";

  static const String _appendTextMethodString = "appendText";

  static const String _appendImageMethodString = "appendImage";

  static const String _appendBarCodeMethodString = "appendBarCode";

  static const String _appendQrCodeMethodString = "appendQrCode";

  static const String _appendPaperFeedMethodString = "appendPaperFeed";

  static const String _appendSeparatorLineMethodString = "appendSeparatorLine";

  static const String _clearPrintBufferMethodString = "clearPrintBuffer";

  static const String _startPrintMethodString = "startPrint";

  /// Whether the printer is available or not.
  static Future<bool> isPrinterAvailable() async {
    try {
      final isTrue =
          await _methodChannel.invokeMethod(_isPrinterAvailableMethodString);
      if (isTrue) {
        return true;
      }

      return false;
    } catch (e) {
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Appends [text] data to print buffer.
  static Future<bool> appendText({
    required String text,
    bool isBoldFont = false,
    bool isLineBreak = true,
    PrinterAlign textAlign = PrinterAlign.left,
    PrinterFontSize fontSize = PrinterFontSize.twentyFour,
  }) async {
    try {
      final result = await _methodChannel.invokeMethod(
        _appendTextMethodString,
        {
          "text": text,
          "isBoldFont": isBoldFont,
          "isLineBreak": isLineBreak,
          "align": textAlign.name.toUpperCase(),
          "fontSize": fontSize.name
        },
      );
      // If operation succeeded, it will return 0, otherwise return something else.
      if (result == 0) {
        return true;
      }

      return false;
    } catch (e) {
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Appends [byteArray] of an image to print buffer.
  ///
  /// By dafault, an image will be printed with full-sized,
  /// which can make it smaller by setting [sampleSize].
  /// For example, setting [sampleSize] to 2 will return 1/2 of the original size.
  static Future<bool> appendImage({
    required Uint8List byteArray,
    PrinterAlign align = PrinterAlign.center,
    int sampleSize = 1,
  }) async {
    try {
      final result = await _methodChannel.invokeMethod(
        _appendImageMethodString,
        {
          "byteArray": byteArray,
          "align": align.name.toUpperCase(),
          "sampleSize": sampleSize
        },
      );

      if (result == 0) {
        return true;
      }

      return false;
    } catch (e) {
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Appends barcode [data] to print buffer.
  static Future<bool> appendBarCode({
    required String data,
    int pixelPoint = 1,
    int height = 150,
    PrinterAlign align = PrinterAlign.center,
  }) async {
    try {
      final result = await _methodChannel.invokeMethod(
        _appendBarCodeMethodString,
        {
          "data": data,
          "pixelPoint": pixelPoint,
          "height": height,
          "align": align.name.toUpperCase(),
        },
      );

      if (result == 0) {
        return true;
      }

      return false;
    } catch (e) {
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Appends QR code [data] to print buffer.
  static Future<bool> appendQrCode({
    required String data,
    required int width,
    required int height,
    int leftOffset = 0,
  }) async {
    try {
      final result =
          await _methodChannel.invokeMethod(_appendQrCodeMethodString, {
        "data": data,
        "width": width,
        "height": height,
        "leftOffset": leftOffset,
      });

      if (result == 0) {
        return true;
      }

      return false;
    } catch (e) {
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Appends paper feed data to print buffer.
  static Future<PrintingResponseCode> appendPaperFeed({
    required int height,
    PrinterPaperFeed paperFeedType = PrinterPaperFeed.normal,
  }) async {
    int type;
    switch (paperFeedType) {
      case PrinterPaperFeed.normal:
        type = 0x00;
        break;
      case PrinterPaperFeed.dotMatrix:
        type = 0x01;
        break;
    }
    try {
      final result = await _methodChannel
          .invokeMethod<String>(_appendPaperFeedMethodString, {
        "height": height,
        "type": type,
      });

      return result.toPrintingResponseCode();
    } catch (e) {
      if (e is MissingPluginException) {
        return PrintingResponseCode.printUnknown;
      }

      rethrow;
    }
  }

  /// Appends a separator line to print buffer.
  static Future<bool> appendSeparatorLine() async {
    try {
      final result =
          await _methodChannel.invokeMethod(_appendSeparatorLineMethodString);

      if (result == 0) {
        return true;
      }

      return false;
    } catch (e) {
      // Suppose the device is not available
      if (e is MissingPluginException) {
        return false;
      }

      rethrow;
    }
  }

  /// Clears current print buffer.
  static Future<void> clearPrintBuffer() async {
    try {
      await _methodChannel.invokeMethod(_clearPrintBufferMethodString);
    } catch (e) {
      if (e is MissingPluginException) {
        return;
      }

      rethrow;
    }
  }

  /// Starts printing operation and return [PrintingResponseCode].
  static Future<PrintingResponseCode> startPrint() async {
    try {
      final result =
          await _methodChannel.invokeMethod<String>(_startPrintMethodString);

      return result.toPrintingResponseCode();
    } catch (e) {
      if (e is MissingPluginException) {
        return PrintingResponseCode.printUnknown;
      }

      rethrow;
    }
  }
}
