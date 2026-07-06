package com.philaphonh.flutter_sunyard_s200

import android.os.Bundle
import android.util.Log
import com.sunyard.api.printer.OnPrintListener

/**
 * Printer Module using Service-based approach
 * This connects to the system service instead of loading native libraries directly
 */
class PrinterModuleService {
    companion object {
        private const val TAG = "S200_PrinterService"

        // Bundle keys for printer options
        private const val KEY_ALIGN = "align"
        private const val KEY_FONT_SIZE = "fontSize"
        private const val KEY_BOLD = "isBold"
        private const val KEY_WIDTH = "width"
        private const val KEY_HEIGHT = "height"
    }

    private val serviceConnection = PrinterServiceConnection.getInstance()

    /** Check if printer is available via service */
    fun isPrinterAvailable(): Boolean {
        return try {
            val connected = serviceConnection.isServiceConnected()
            Log.d(TAG, "Printer available via service: $connected")
            connected
        } catch (e: Exception) {
            Log.e(TAG, "Error checking printer availability: ${e.message}", e)
            false
        }
    }

    /** Connect to the printer service */
    fun connect(context: android.content.Context): Boolean {
        return try {
            Log.d(TAG, "Connecting to printer service...")
            serviceConnection.connect(context)
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to service: ${e.message}", e)
            false
        }
    }

    /** Disconnect from the printer service */
    fun disconnect() {
        serviceConnection.disconnect()
    }

    /** Create a Bundle with formatting options */
    private fun createPrintBundle(align: Int = 0, fontSize: Int = 24, isBold: Boolean = false): Bundle {
        return Bundle().apply {
            putInt(KEY_ALIGN, align)
            putInt(KEY_FONT_SIZE, fontSize)
            putBoolean(KEY_BOLD, isBold)
        }
    }

    /** Add text to print buffer */
    fun addText(text: String, align: Int, fontSize: Int): Int {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for addText")
                return -1
            }
            val bundle = createPrintBundle(align, fontSize)
            printer.addText(bundle, text)
            Log.d(TAG, "addText success: '$text'")
            0
        } catch (e: Exception) {
            Log.e(TAG, "Error in addText: ${e.message}", e)
            -1
        }
    }

    /** Add image to print buffer */
    fun addImage(byteArray: ByteArray, align: Int): Int {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for addImage")
                return -1
            }
            val bundle = createPrintBundle(align = align)
            printer.addImage(bundle, byteArray)
            Log.d(TAG, "addImage success")
            0
        } catch (e: Exception) {
            Log.e(TAG, "Error in addImage: ${e.message}", e)
            -1
        }
    }

    /** Add barcode to print buffer */
    fun addBarCode(data: String, pixelPoint: Int, height: Int, align: Int): Int {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for addBarCode")
                return -1
            }
            val bundle = Bundle().apply {
                putInt(KEY_ALIGN, align)
                putInt(KEY_WIDTH, pixelPoint)
                putInt(KEY_HEIGHT, height)
            }
            printer.addBarCode(bundle, data)
            Log.d(TAG, "addBarCode success: '$data'")
            0
        } catch (e: Exception) {
            Log.e(TAG, "Error in addBarCode: ${e.message}", e)
            -1
        }
    }

    /** Add QR code to print buffer */
    fun addQrCode(data: String, width: Int, height: Int): Int {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for addQrCode")
                return -1
            }
            val bundle = Bundle().apply {
                putInt(KEY_WIDTH, width)
                putInt(KEY_HEIGHT, height)
            }
            printer.addQrCode(bundle, data)
            Log.d(TAG, "addQrCode success: '$data'")
            0
        } catch (e: Exception) {
            Log.e(TAG, "Error in addQrCode: ${e.message}", e)
            -1
        }
    }

    /** Feed paper */
    fun feedPaper(lines: Int): String {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for feedPaper")
                return "ERROR"
            }
            printer.feedLine(lines)
            Log.d(TAG, "feedPaper success: $lines lines")
            "OK"
        } catch (e: Exception) {
            Log.e(TAG, "Error in feedPaper: ${e.message}", e)
            "ERROR"
        }
    }

    /** Start printing */
    fun startPrint(): String {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for startPrint")
                return "ERROR"
            }
            // Create a simple callback listener
            val listener = object : OnPrintListener.Stub() {
                override fun onFinish() {
                    Log.d(TAG, "Print finished successfully")
                }

                override fun onError(errorCode: Int) {
                    Log.e(TAG, "Print failed with error code: $errorCode")
                }
            }
            printer.startPrint(listener)
            Log.d(TAG, "startPrint initiated")
            "OK"
        } catch (e: Exception) {
            Log.e(TAG, "Error in startPrint: ${e.message}", e)
            "ERROR"
        }
    }

    /** Get printer status */
    fun getStatus(): Int {
        return try {
            val printer = serviceConnection.getPrinterService()
            if (printer == null) {
                Log.e(TAG, "Printer service not available for getStatus")
                return -1
            }
            val status = printer.getStatus()
            Log.d(TAG, "Printer status: $status")
            status
        } catch (e: Exception) {
            Log.e(TAG, "Error in getStatus: ${e.message}", e)
            -1
        }
    }

    /** Clear print buffer */
    fun clearPrintBuffer() {
        // Service-based approach may not have explicit clear,
        // this would need to be implemented based on actual service capabilities
        Log.d(TAG, "clearPrintBuffer called (may not be supported by service)")
    }

    /** Add separator line */
    fun addSeparatorLine(): Int {
        // Service-based approach - add a separator as text
        return addText("----------------------------------------", 1, 24)
    }
}
