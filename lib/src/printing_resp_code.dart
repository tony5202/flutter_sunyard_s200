/// Printing response code according to official docs.
enum PrintingResponseCode {
  /// No content to print.
  printNoContent,

  /// Printing operation succeeded.
  printSuccess,

  /// Unknown error.
  printUnknown,

  /// Bitmap is too wide, it needs to be lower than 384.
  printerBitmapTooWide,

  /// Printer is busy.
  printerBusy,

  /// Printing operation is failed.
  printerFail,

  /// No font detected.
  printerNoTTF,

  /// No paper left to print.
  printerPaperLack,

  /// Paper type error.
  printerPaperTypeError,

  /// Low battery warning.
  printerVolLowWarning,

  /// Battery is too low to perform the operation.
  printerVolTooLow,

  /// Printer's temperature is too high to perform the operation.
  printerTooHot
}
