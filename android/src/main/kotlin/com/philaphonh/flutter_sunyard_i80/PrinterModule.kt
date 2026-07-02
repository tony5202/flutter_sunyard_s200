package com.philaphonh.flutter_sunyard_i80

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
    /** An instance of [Printer2].
     * Since getting an instance with context will cause SharedPreferences issue,
     * I have to get an instance from deprecated method instead.
     * */
    private var printerInstance: Printer2 = Printer2.getInstance()

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
        return printerInstance.havePrinter()
    }

    /** Append text entity to print buffer. */
    fun appendText(text: String, isBold: Boolean, isLineBreak: Boolean, align: Align, fontSize: FontLattice): Int {
        val textEntity = TextEntity()
        textEntity.text = text
        textEntity.fontsize = fontSize
        textEntity.align = align
        textEntity.isBoldFont = isBold
        textEntity.isLineBreak = isLineBreak
        textEntity.fontsize = fontSize

        return printerInstance.appendTextEntity2(textEntity)
    }

    /** Append image data to print buffer. */
    fun appendImage(byteArray: ByteArray, align: Align, sampleSize: Int): Int {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = sampleSize

        val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, bitmapOptions)

        return printerInstance.appendImage(bitmap, align)
    }

    /** Append barcode to print buffer. */
    fun appendBarCode(data: String, pixelPoint: Int, height: Int, align: Align): Int {
        // This printer supports only 128
        return printerInstance.appendBarCodeByPixel(pixelPoint, height, align, 128, data)
    }

    /** Append single QR code to print buffer. */
    fun appendQrCode(data: String, width: Int, height: Int, leftOffset: Int): Int {
        return printerInstance.appendQrCode(width, height, leftOffset, data)
    }

    /** Append paper feed to print buffer. */
    fun appendPaperFeed(type: Int, height: Int): String {
        return printerInstance.appendTakePaper(type, height).name
    }

    /** Append a separator line to print buffer. */
    fun appendSeparatorLine(): Int {
        return printerInstance.appendTextEntity2(printerInstance.separatorLinetEntity)
    }

    /** Clear current print buffer. */
    fun clearPrintBuffer() {
        printerInstance.printBuffer.clear()
    }

    /** Start printing operation. */
    fun startPrint(): String {
        return printerInstance.startPrint().name
    }
}