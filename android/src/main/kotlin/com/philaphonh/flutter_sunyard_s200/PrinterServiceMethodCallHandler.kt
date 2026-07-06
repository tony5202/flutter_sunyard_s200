package com.philaphonh.flutter_sunyard_s200

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

class PrinterServiceMethodCallHandler(printerService: PrinterModuleService) : MethodCallHandler {
    /** An instance of [PrinterModuleService] */
    private lateinit var printerService: PrinterModuleService

    init {
        this.printerService = printerService
    }

    /** Convert string align to integer: left=0, center=1, right=2 */
    private fun parseAlign(align: Any?): Int {
        return when (align) {
            is Int -> align
            is String -> when (align.lowercase()) {
                "left" -> 0
                "center" -> 1
                "right" -> 2
                else -> 0
            }
            else -> 0
        }
    }

    /** Parse font size from string or int */
    private fun parseFontSize(fontSize: Any?): Int {
        return when (fontSize) {
            is Int -> fontSize
            is String -> when (fontSize.lowercase()) {
                "eight" -> 8
                "sixteen" -> 16
                "eighteen" -> 18
                "twenty" -> 20
                "twentytwo" -> 22
                "twentyfour" -> 24
                "twentysix" -> 26
                "twentyeight" -> 28
                "thirty" -> 30
                "thirtytwo" -> 32
                "thirtysix" -> 36
                "forty" -> 40
                "fortyfour" -> 44
                "fortyeight" -> 48
                else -> 24
            }
            else -> 24
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        android.util.Log.d("S200_PrinterServiceHandler", "Method called: ${call.method}")
        when (call.method) {
            "isPrinterAvailable" -> {
                try {
                    val available = printerService.isPrinterAvailable()
                    android.util.Log.d("S200_PrinterServiceHandler", "isPrinterAvailable result: $available")
                    result.success(available)
                } catch (e: Exception) {
                    android.util.Log.e("S200_PrinterServiceHandler", "isPrinterAvailable error: ${e.message}", e)
                    result.error("PRINTER_ERROR", e.message, null)
                }
            }
            "appendText" -> {
                val text = call.argument<String>("text")
                val align = parseAlign(call.argument("align"))
                val fontSize = parseFontSize(call.argument("fontSize"))

                android.util.Log.d("S200_PrinterServiceHandler", "appendText: text='$text', align=$align, fontSize=$fontSize")
                
                if (text != null) {
                    result.success(printerService.addText(text, align, fontSize))
                } else {
                    result.error("INVALID_ARGUMENT", "text parameter is required", null)
                }
            }
            "appendImage" -> {
                val byteArray = call.argument<ByteArray>("byteArray")
                val align = parseAlign(call.argument("align"))

                android.util.Log.d("S200_PrinterServiceHandler", "appendImage: align=$align")
                
                if (byteArray != null) {
                    result.success(printerService.addImage(byteArray, align))
                } else {
                    result.error("INVALID_ARGUMENT", "byteArray parameter is required", null)
                }
            }
            "appendBarCode" -> {
                val data = call.argument<String>("data")
                val pixelPoint = call.argument<Int>("pixelPoint") ?: 2
                val height = call.argument<Int>("height") ?: 100
                val align = parseAlign(call.argument("align"))

                android.util.Log.d("S200_PrinterServiceHandler", "appendBarCode: data='$data', align=$align")
                
                if (data != null) {
                    result.success(printerService.addBarCode(data, pixelPoint, height, align))
                } else {
                    result.error("INVALID_ARGUMENT", "data parameter is required", null)
                }
            }
            "appendQrCode" -> {
                val data = call.argument<String>("data")
                val width = call.argument<Int>("width") ?: 200
                val height = call.argument<Int>("height") ?: 200

                android.util.Log.d("S200_PrinterServiceHandler", "addQrCode: data='$data'")
                
                if (data != null) {
                    result.success(printerService.addQrCode(data, width, height))
                } else {
                    result.error("INVALID_ARGUMENT", "data parameter is required", null)
                }
            }
            "appendPaperFeed" -> {
                val lines = call.argument<Int>("lines") ?: 3
                result.success(printerService.feedPaper(lines))
            }
            "appendSeparatorLine" -> result.success(printerService.addSeparatorLine())
            "clearPrintBuffer" -> {
                printerService.clearPrintBuffer()
                result.success(null)
            }
            "startPrint" -> result.success(printerService.startPrint())
            else -> result.notImplemented()
        }
    }
}
