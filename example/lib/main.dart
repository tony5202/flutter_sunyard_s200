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
}
