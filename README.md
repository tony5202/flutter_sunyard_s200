# flutter_sunyard_i80

A Flutter plugin that helps you utilize functionalities on a Sunyard i80 EDC POS terminal.

## Features

- Print a line of text, image, barcode, QR code, etc.
- Retrieve the real serial number of the device.

## Usage

### Installation

Add this line to your `pubspec.yaml` file:

```yaml
flutter_sunyard_i80: ^1.0.0
```

Alternatively, you can add these lines if you want to install it directly from the GitHub repo:

```yaml
flutter_sunyard_i80:
  git: 
    url: https://github.com/philaphonh/flutter_sunyard_i80.git
    ref: main
```

### Printing

It is recommended to check the printer status before printing:

```dart
import 'package:flutter_sunyard_i80/flutter_sunyard_i80.dart' as i80;

final isPrinterAvailable = await i80.Printer.isPrinterAvailable();

if (isPrinterAvailable) {
  // Do stuffs here...
}
```

Append text to print buffer:

```dart
await Printer.appendText(
  text: "Flutter",
  textAlign: PrinterAlign.left,
);

await Printer.appendText(
  text: "Flutter",
  textAlign: PrinterAlign.left, // Adjust text alignment
  isBoldFont: true, // Make text bold
);

await Printer.appendText(
  text: "Flutter",
  textAlign: PrinterAlign.right,
  isLineBreak: false, // Line break is added by default
  fontSize: PrinterFontSize.thirtySix, // Adjust font size based on available size provided by the printer
);

```

Append image to print buffer:

```dart
// Load an asset image
final assetImage = await rootBundle.load("/path/to/the/asset");

// Convert buffer to bytes list
final bytes = Uint8List.view(assetImage.buffer);

// Append image bytes to the print buffer
await Printer.appendImage(
      byteArray: bytes,
      align: PrinterAlign.right,
);

await Printer.appendImage(
  byteArray: bytes,
  align: PrinterAlign.left,
  sampleSize: 2, // Adjust image size based on Android's [inSampleSize] prop of [BitMapFactory.Options]. More info can be found at https://developer.android.com/reference/android/graphics/BitmapFactory.Options
);
```

Append barcode data to print buffer

```dart
await Printer.appendBarCode(data: "1234567890");
```

Append QR data to print buffer

```dart
await Printer.appendQrCode(
  data: "https://artyboy.dev",
  width: 100, // Adjust QR width
  height: 100, // Adjust QR height
);
```

Start printing

```dart
// Always execute this function after appending all data
await Printer.startPrint();
```

### Retrieving Serial Number

To retrieve the device's serial number:

```dart
String serialNumber = "";
final isDeviceAvailable = await i80.Device.isAvailable();

if (isDeviceAvailable) {
  serialNumber = await i80.TerminalInfo.serialNumber;
}
```
