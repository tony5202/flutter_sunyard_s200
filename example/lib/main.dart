import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:flutter_sunyard_i80/flutter_sunyard_i80.dart' as i80;

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
    final isDeviceAvailable = await i80.Device.isAvailable();
    final isPrinterAvailable = await i80.Printer.isPrinterAvailable();
    serialNumber = await i80.TerminalInfo.serialNumber;
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
          title: const Text('flutter_sunyard_i80 Example'),
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
      await i80.Printer.appendText(
        text: "Flutter",
        textAlign: i80.PrinterAlign.left,
      );

      await i80.Printer.appendText(
        text: "Flutter",
        textAlign: i80.PrinterAlign.right,
      );

      await i80.Printer.appendText(
        text: "Flutter",
        textAlign: i80.PrinterAlign.left,
        isBoldFont: true,
      );
      await i80.Printer.appendText(
        text: "Flutter",
        textAlign: i80.PrinterAlign.right,
        isLineBreak: false,
        fontSize: i80.PrinterFontSize.thirtySix,
      );

      await i80.Printer.startPrint();
    } catch (e) {
      rethrow;
    }
  }

  void printImage() async {
    // An image must have white background
    final assetImage = await rootBundle.load("assets/wojak.jpg");

    final bytes = Uint8List.view(assetImage.buffer);

    await i80.Printer.appendImage(
      byteArray: bytes,
      align: i80.PrinterAlign.right,
    );

    await i80.Printer.appendImage(
      byteArray: bytes,
      align: i80.PrinterAlign.left,
      sampleSize: 2,
    );

    await i80.Printer.startPrint();
  }

  void printBarcode() async {
    await i80.Printer.appendBarCode(data: "1234567890");

    await i80.Printer.startPrint();
  }

  void printQr() async {
    await i80.Printer.appendQrCode(
      data: "https://artyboy.dev",
      width: 100,
      height: 100,
    );

    await i80.Printer.startPrint();
  }
}
