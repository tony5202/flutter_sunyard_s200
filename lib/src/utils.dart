import 'printing_resp_code.dart';

extension ValueParsing on String? {
  /// Return [PrintingResponseCode] by [result].
  PrintingResponseCode toPrintingResponseCode() {
    switch (this) {
      case "Print_Success":
        return PrintingResponseCode.printSuccess;
      case "Print_NO_CONTENT":
        return PrintingResponseCode.printNoContent;
      case "Print_Bitmap_TooWide":
        return PrintingResponseCode.printerBitmapTooWide;
      case "Printer_Busy":
        return PrintingResponseCode.printerBusy;
      case "Printer_Fail":
        return PrintingResponseCode.printerFail;
      case "Printer_NO_TTF":
        return PrintingResponseCode.printerNoTTF;
      case "Printer_PaperLack":
        return PrintingResponseCode.printerPaperLack;
      case "Printer_PaperTypeError":
        return PrintingResponseCode.printerPaperTypeError;
      case "Printer_Vol_LowWarning":
        return PrintingResponseCode.printerVolLowWarning;
      case "Printer_Vol_TooLow":
        return PrintingResponseCode.printerVolTooLow;
      case "Printre_TooHot":
        return PrintingResponseCode.printerTooHot;
      default:
        return PrintingResponseCode.printUnknown;
    }
  }
}
