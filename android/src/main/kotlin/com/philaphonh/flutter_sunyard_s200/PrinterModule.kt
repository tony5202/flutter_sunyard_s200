package com.philaphonh.flutter_sunyard_s200

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.socsi.exception.SDKException
import com.socsi.smartposapi.printer.Align
import com.socsi.smartposapi.printer.FontLattice
import com.socsi.smartposapi.printer.Printer2
import com.socsi.smartposapi.printer.TextEntity

/** A module to utilize printer functionality. */
class PrinterModule {
    init {
        android.util.Log.e("S200_PrinterModule", "=== PRINTER MODULE INITIALIZED ===")
    }

    /** An instance of [Printer2].
     * Since getting an instance with context will cause SharedPreferences issue,
     * I have to get an instance from deprecated method instead.
     * */
    private var printerInstance: Printer2? = null

    /** Get printer instance lazily */
    private fun getPrinter(): Printer2? {
        if (printerInstance == null) {
            try {
                printerInstance = Printer2.getInstance()
                android.util.Log.d("S200_PrinterModule", "Printer instance created")
            } catch (e: Exception) {
                android.util.Log.e("S200_PrinterModule", "Error getting printer instance: ${e.message}", e)
            }
        }
        return printerInstance
    }

    /** Method string of [isPrinterAvailable]. */
    val isPrinterAvailableMethodString = "isPrinterAvailable"

    /** Method string of [appendText] */
    val appendTextMethodString = "appendText"

    /** Method string of [appendImage] */
    val appendImageMethodString = "appendImage"

    /** Method string of [appendBarCode] */
    val appendBarCodeMethodString = "appendBarCode"

    /** Method string of [appendQrCode] */
    val appendQrCodeMethodString = "appendQrCode"

    /** Method string of [appendPaperFeed] */
    val appendPaperFeedMethodString = "appendPaperFeed"

    /** Method string of [appendSeparatorLine] */
    val appendSeparatorLineMethodString = "appendSeparatorLine"

    /** Method string of [startPrint] */
    val startPrintMethodString = "startPrint"

    /** Method string of [clearPrintBuffer] */
    val clearPrintBufferMethodString = "clearPrintBuffer"

    /** Whether printer is available or not. */
    fun isPrinterAvailable(): Boolean {
        try {
            val printer = getPrinter() ?: return false
            val hasPrinter = printer.havePrinter()
            android.util.Log.d("S200_PrinterModule", "havePrinter result: $hasPrinter")
            return hasPrinter
        } catch (e: Exception) {
            android.util.Log.e("S200_PrinterModule", "Error checking printer availability: ${e.message}", e)
            return false
        }
    }

    /** Append text entity to print buffer. */
    fun appendText(text: String, isBold: Boolean, isLineBreak: Boolean, align: Align, fontSize: FontLattice): Int {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendText")
            return -1
        }
        val textEntity = TextEntity()
        textEntity.text = text
        textEntity.fontsize = fontSize
        textEntity.align = align
        textEntity.isBoldFont = isBold
        textEntity.isLineBreak = isLineBreak
        textEntity.fontsize = fontSize

        return printer.appendTextEntity2(textEntity)
    }

    /** Append image data to print buffer. */
    fun appendImage(byteArray: ByteArray, align: Align, sampleSize: Int): Int {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendImage")
            return -1
        }
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = sampleSize

        val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, bitmapOptions)

        return printer.appendImage(bitmap, align)
    }

    /** Append barcode to print buffer. */
    fun appendBarCode(data: String, pixelPoint: Int, height: Int, align: Align): Int {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendBarCode")
            return -1
        }
        // This printer supports only 128
        return printer.appendBarCodeByPixel(pixelPoint, height, align, 128, data)
    }

    /** Append single QR code to print buffer. */
    fun appendQrCode(data: String, width: Int, height: Int, leftOffset: Int): Int {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendQrCode")
            return -1
        }
        return printer.appendQrCode(width, height, leftOffset, data)
    }

    /** Append paper feed to print buffer. */
    fun appendPaperFeed(type: Int, height: Int): String {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendPaperFeed")
            return "ERROR"
        }
        return printer.appendTakePaper(type, height).name
    }

    /** Append a separator line to print buffer. */
    fun appendSeparatorLine(): Int {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for appendSeparatorLine")
            return -1
        }
        return printer.appendTextEntity2(printer.separatorLinetEntity)
    }

    /** Clear current print buffer. */
    fun clearPrintBuffer() {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for clearPrintBuffer")
            return
        }
        printer.printBuffer.clear()
    }

    /** Start printing operation. */
    fun startPrint(): String {
        val printer = getPrinter()
        if (printer == null) {
            android.util.Log.e("S200_PrinterModule", "Printer not available for startPrint")
            return "ERROR"
        }
        return printer.startPrint().name
    }
}
