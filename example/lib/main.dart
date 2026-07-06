import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:flutter_sunyard_s200/flutter_sunyard_s200.dart' as s200;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool isPrinterAvailable = false;

  bool isDeviceAvailable = false;

  String serialNumber = "";

  @override
  void initState() {
    super.initState();
    initPage();
  }

  void initPage() async {
    final isDeviceAvailable = await s200.Device.isAvailable();
    final isPrinterAvailable = await s200.Printer.isPrinterAvailable();
    serialNumber = await s200.TerminalInfo.serialNumber;
    if (isDeviceAvailable) {
      setState(() {
        this.isDeviceAvailable = isDeviceAvailable;
      });
    }
    if (isPrinterAvailable) {
      setState(() {
        this.isPrinterAvailable = isPrinterAvailable;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('flutter_sunyard_s200 Example'),
        ),
        body: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Center(
                child: Text('Device status: ${deviceStatus()}'),
              ),
              Center(
                child: Text('Device Serial Number: $serialNumber'),
              ),
              Center(
                child: Text('Printer status: ${printerStatus()}'),
              ),
              Center(
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                      onPressed: () {
                        printText();
                      },
                      child: const Text('Print Text')),
                ),
              ),
              Center(
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                      onPressed: () {
                        printImage();
                      },
                      child: const Text('Print Image')),
                ),
              ),
              Center(
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                      onPressed: () {
                        printBarcode();
                      },
                      child: const Text('Print Barcode')),
                ),
              ),
              Center(
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                      onPressed: () {
                        printQr();
                      },
                      child: const Text('Print QR')),
                ),
              ),
              const SizedBox(height: 16),
              Center(
                child: SizedBox(
                  width: double.infinity,
                  child: FilledButton(
                      onPressed: () {
                        printFullTestReceipt();
                      },
                      child: const Text('📋 Print Full Receipt Test')),
                ),
              ),
              const SizedBox(height: 8),
              // ปุ่มทดสอบแยก
              Row(
                children: [
                  Expanded(
                    child: FilledButton(
                        onPressed: () {
                          testPrintText();
                        },
                        child: const Text('Text')),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: FilledButton(
                        onPressed: () {
                          testPrintBarcode();
                        },
                        child: const Text('Barcode')),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: FilledButton(
                        onPressed: () {
                          testPrintQR();
                        },
                        child: const Text('QR')),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: FilledButton(
                        onPressed: () {
                          testPrintImage();
                        },
                        child: const Text('Image')),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  String deviceStatus() {
    return isDeviceAvailable ? "Available" : "Unavailable";
  }

  String printerStatus() {
    return isPrinterAvailable ? "Available" : "Unavailable";
  }

  void printText() async {
    try {
      await s200.Printer.appendText(
        text: "Flutter",
        textAlign: s200.PrinterAlign.left,
      );

      await s200.Printer.appendText(
        text: "Flutter",
        textAlign: s200.PrinterAlign.right,
      );

      await s200.Printer.appendText(
        text: "Flutter",
        textAlign: s200.PrinterAlign.left,
        isBoldFont: true,
      );
      await s200.Printer.appendText(
        text: "Flutter",
        textAlign: s200.PrinterAlign.right,
        isLineBreak: false,
        fontSize: s200.PrinterFontSize.thirtySix,
      );

      await s200.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }

  void printImage() async {
    // An image must have white background
    final assetImage = await rootBundle.load("assets/wojak.jpg");

    final bytes = Uint8List.view(assetImage.buffer);

    await s200.Printer.appendImage(
      byteArray: bytes,
      align: s200.PrinterAlign.right,
    );

    await s200.Printer.appendImage(
      byteArray: bytes,
      align: s200.PrinterAlign.left,
      sampleSize: 2,
    );

    await s200.Printer.startPrint();
  }

  void printBarcode() async {
    await s200.Printer.appendBarCode(data: "1234567890");

    await s200.Printer.startPrint();
  }

  void printQr() async {
    await s200.Printer.appendQrCode(
      data: "https://artyboy.dev",
      width: 100,
      height: 100,
    );

    await s200.Printer.startPrint();
  }

  void printFullTestReceipt() async {
    try {
      final isPrinterAvailable = await s200.Printer.isPrinterAvailable();
      if (!isPrinterAvailable) {
        print('printer not available');
        return;
      }

      await s200.Printer.clearPrintBuffer();

      await s200.Printer.appendText(
        text: "TEST STORE",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 5);

      await s200.Printer.appendSeparatorLine();

      await s200.Printer.appendText(
        text: "123 Test Street, Bangkok",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendText(
        text: "Tel: 02-123-4567",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendSeparatorLine();

      await s200.Printer.appendText(
        text: "RECEIPT TEST",
        textAlign: s200.PrinterAlign.left,
        fontSize: s200.PrinterFontSize.twentyFour,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 3);
      await s200.Printer.appendText(
        text: "Tony ....DER",
        textAlign: s200.PrinterAlign.left,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendText(
        text: "Item 1...................................100.00",
        textAlign: s200.PrinterAlign.left,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendText(
        text: "Item 2...................................150.00",
        textAlign: s200.PrinterAlign.left,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      // 9. รายการสินค้า 3
      await s200.Printer.appendText(
        text: "Item 3...................................200.00",
        textAlign: s200.PrinterAlign.left,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendSeparatorLine();

      await s200.Printer.appendText(
        text: "TOTAL...................................450.00",
        textAlign: s200.PrinterAlign.right,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendSeparatorLine();

      await s200.Printer.appendPaperFeed(height: 5);

      await s200.Printer.appendText(
        text: DateTime.now().toString(),
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      await s200.Printer.appendText(
        text: "Serial: $serialNumber",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.twentyFour,
      );

      // 14. Barcode
      await s200.Printer.appendPaperFeed(height: 10);
      await s200.Printer.appendBarCode(
        data: "1234567890",
        pixelPoint: 2,
        height: 100,
        align: s200.PrinterAlign.center,
      );

      // 15. QR Code
      await s200.Printer.appendPaperFeed(height: 10);
      await s200.Printer.appendQrCode(
        data: "https://flutter.dev",
        width: 150,
        height: 150,
        leftOffset: 100,
      );

      await s200.Printer.appendPaperFeed(height: 30);

      await s200.Printer.appendText(
        text: "*** THANK YOU ***",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.twentyFour,
        isBoldFont: true,
      );

      final result = await s200.Printer.startPrint();
      print('printrd: $result');
    } catch (e) {
      print('erorr: $e');
      rethrow;
    }
  }

  void testPrintText() async {
    try {
      await s200.Printer.clearPrintBuffer();

      await s200.Printer.appendText(
        text: "TEXT TEST - S200",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 10);

      await s200.Printer.appendText(
        text: "Normal size text",
        textAlign: s200.PrinterAlign.left,
      );

      await s200.Printer.appendText(
        text: "Bold text",
        textAlign: s200.PrinterAlign.left,
        isBoldFont: true,
      );

      await s200.Printer.appendText(
        text: "Right aligned",
        textAlign: s200.PrinterAlign.right,
      );

      await s200.Printer.appendPaperFeed(height: 30);
      await s200.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }

  void testPrintBarcode() async {
    try {
      await s200.Printer.clearPrintBuffer();

      await s200.Printer.appendText(
        text: "BARCODE TEST",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 10);

      await s200.Printer.appendBarCode(
        data: "1234567890",
        pixelPoint: 2,
        height: 80,
        align: s200.PrinterAlign.center,
      );

      await s200.Printer.appendPaperFeed(height: 30);
      await s200.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }

  void testPrintQR() async {
    try {
      await s200.Printer.clearPrintBuffer();

      await s200.Printer.appendText(
        text: "QR CODE TEST",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 10);

      await s200.Printer.appendQrCode(
        data: "https://flutter.dev/docs",
        width: 150,
        height: 150,
        leftOffset: 80,
      );

      await s200.Printer.appendPaperFeed(height: 30);
      await s200.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }

  // ทดสอบพิมพ์รูปภาพ
  void testPrintImage() async {
    try {
      await s200.Printer.clearPrintBuffer();

      await s200.Printer.appendText(
        text: "IMAGE TEST",
        textAlign: s200.PrinterAlign.center,
        fontSize: s200.PrinterFontSize.thirtySix,
        isBoldFont: true,
      );

      await s200.Printer.appendPaperFeed(height: 10);

      final assetImage = await rootBundle.load("assets/wojak.jpg");
      final bytes = Uint8List.view(assetImage.buffer);

      await s200.Printer.appendImage(
        byteArray: bytes,
        align: s200.PrinterAlign.center,
        sampleSize: 2,
      );

      await s200.Printer.appendPaperFeed(height: 30);
      await s200.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }
}
