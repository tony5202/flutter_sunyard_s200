package com.philaphonh.flutter_sunyard_i80

import com.socsi.exception.SDKException
import com.socsi.smartposapi.printer.Align
import com.socsi.smartposapi.printer.FontLattice
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class PrinterMethodCallHandler(printerModule: PrinterModule) : MethodCallHandler {
    /** An instance of [PrinterModule] */
    private lateinit var printerModule: PrinterModule

    init {
        this.printerModule = printerModule
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            printerModule.isPrinterAvailableMethodString -> {
                try {
                    result.success(printerModule.isPrinterAvailable())
                } catch (e: SDKException) {
                    result.error(e.errCode, e.message, null)
                }
            }
            printerModule.appendTextMethodString -> {
                val text = call.argument<String>("text")
                val isBoldFont = call.argument<Boolean>("isBoldFont")
                val isLineBreak = call.argument<Boolean>("isLineBreak")
                val align = call.argument<String>("align")
                val fontSize = when (call.argument<String>("fontSize")) {
                    "eight" -> FontLattice.EIGHT
                    "sixteen" -> FontLattice.SIXTEEN
                    "eighteen" -> FontLattice.EIGHTEEN
                    "twenty" -> FontLattice.TWENTY
                    "twentyTwo" -> FontLattice.TWENTY_TWO
                    "twentyFour" -> FontLattice.TWENTY_FOUR
                    "twentySix" -> FontLattice.TWENTY_SIX
                    "twentyEight" -> FontLattice.TWENTY_EIGHT
                    "thirty" -> FontLattice.THIRTY
                    "thirtyTwo" -> FontLattice.THIRTY_TWO
                    "thirtySix" -> FontLattice.THIRTY_SIX
                    "forty" -> FontLattice.FORTY
                    "fortyFour" -> FontLattice.FORTY_FOUR
                    "fortyEight" -> FontLattice.FORTY_EIGHT
                    else -> {
                        FontLattice.TWENTY_FOUR
                    }
                }

                if (text != null && isBoldFont != null && isLineBreak != null && align != null && fontSize != null) {
                    result.success(printerModule.appendText(text, isBoldFont, isLineBreak, Align.valueOf(align), fontSize))
                }
            }
            printerModule.appendImageMethodString -> {
                val byteArray = call.argument<ByteArray>("byteArray")
                val align = call.argument<String>("align")
                val sampleSize = call.argument<Int>("sampleSize")

                if (byteArray != null && align != null && sampleSize != null) {
                    result.success(printerModule.appendImage(byteArray, Align.valueOf(align), sampleSize))
                }
            }
            printerModule.appendBarCodeMethodString -> {
                val data = call.argument<String>("data")
                val pixelPoint = call.argument<Int>("pixelPoint")
                val height = call.argument<Int>("height")
                val align = call.argument<String>("align")

                if (data != null && pixelPoint != null && height != null && align != null) {
                    result.success(printerModule.appendBarCode(data, pixelPoint, height, Align.valueOf(align)))
                }
            }
            printerModule.appendQrCodeMethodString -> {
                val data = call.argument<String>("data")
                val width = call.argument<Int>("width")
                val height = call.argument<Int>("height")
                val leftOffset = call.argument<Int>("leftOffset")

                if (data != null && width != null && height != null && leftOffset != null) {
                    result.success(printerModule.appendQrCode(data, width, height, leftOffset))
                }
            }
            printerModule.appendPaperFeedMethodString -> {
                val type = call.argument<Int>("type")
                val height = call.argument<Int>("height")

                if (type != null && height != null) {
                    result.success(printerModule.appendPaperFeed(type,height))
                }
            }
            printerModule.appendSeparatorLineMethodString -> result.success(printerModule.appendSeparatorLine())
            printerModule.clearPrintBufferMethodString -> {
                printerModule.clearPrintBuffer()
                result.success(null)
            }
            printerModule.startPrintMethodString -> result.success(printerModule.startPrint())
            else -> result.notImplemented()
        }
    }
}