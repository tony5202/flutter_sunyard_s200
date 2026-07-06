package com.sunyard.api.printer;

import android.os.Bundle;
import com.sunyard.api.printer.OnPrintListener;
import com.sunyard.api.printer.PrinterChip;
import java.util.List;

/**
 * AIDL interface for Sunyard S200 Printer Service
 * This binds to com.sunyard.api.device_service
 */
interface IPrinter {
    // Get printer status
    int getStatus();

    // Set gray level (0-?)
    void setGray(int gray);

    // Add text to print buffer
    // bundle: contains formatting options
    // str: text content
    void addText(in Bundle bundle, in String str);

    // Add barcode to print buffer
    // bundle: contains formatting options
    // str: barcode data
    void addBarCode(in Bundle bundle, in String str);

    // Add QR code to print buffer
    // bundle: contains formatting options
    // str: QR code data
    void addQrCode(in Bundle bundle, in String str);

    // Add image to print buffer
    // bundle: contains formatting options
    // imageData: image byte array
    void addImage(in Bundle bundle, in byte[] imageData);

    // Feed paper lines
    void feedLine(int lines);

    // Start printing with callback
    void startPrint(OnPrintListener listener);

    // Cut paper
    void cutPaper();

    // Auto cut paper
    void autoCutPaper();

    // Initialize printer
    void initPrinter();

    // Add text chips (complex text formatting)
    int addTextChips(in List<PrinterChip> chips);
}
