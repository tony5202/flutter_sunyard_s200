package com.philaphonh.flutter_sunyard_s200

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import com.sunyard.api.printer.IPrinter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Manages connection to the Sunyard Device Service
 *
 * Architecture:
 * 1. Bind to IDeviceService (the main device service)
 * 2. Call getPrinter() to get the printer binder
 * 3. Convert binder to IPrinter interface
 *
 * Uses reflection to avoid AIDL compilation issues for IDeviceService.
 */
class PrinterServiceConnection private constructor() {
    companion object {
        private const val TAG = "S200_ServiceConnection"
        private const val SERVICE_ACTION = "com.sunyard.api.device_service"
        private const val SERVICE_PACKAGE = "com.sunyard.deviceservice"
        private const val DESCRIPTOR = "com.sunyard.api.IDeviceService"
        private const val TRANSACTION_GET_PRINTER = 10

        @Volatile
        private var instance: PrinterServiceConnection? = null

        fun getInstance(): PrinterServiceConnection {
            return instance ?: synchronized(this) {
                instance ?: PrinterServiceConnection().also { instance = it }
            }
        }
    }

    private var deviceServiceBinder: IBinder? = null
    private var printerService: IPrinter? = null
    private var isConnected = false
    private var context: Context? = null
    private var bindCount = 0
    private var connectionLatch: CountDownLatch? = null
    private var connectionResult: Boolean = false

    /**
     * Connect to the device service and obtain printer interface
     * @return true if connection succeeded
     */
    fun connect(context: Context): Boolean {
        this.context = context.applicationContext

        if (isConnected && printerService != null) {
            bindCount++
            Log.d(TAG, "Already connected, bind count: $bindCount")
            return true
        }

        connectionLatch = CountDownLatch(1)
        connectionResult = false

        try {
            val intent = Intent(SERVICE_ACTION).apply {
                setPackage(SERVICE_PACKAGE)
            }

            Log.d(TAG, "Attempting to bind to device service: $SERVICE_ACTION")
            val result = context.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )

            if (result) {
                Log.d(TAG, "Bind service initiated, waiting for connection...")
                val connected = connectionLatch?.await(5, TimeUnit.SECONDS) ?: false
                if (connected) {
                    bindCount = 1
                    Log.d(TAG, "Successfully connected to device service and obtained printer!")
                } else {
                    Log.e(TAG, "Connection timeout!")
                }
                return connected
            } else {
                Log.e(TAG, "Failed to bind to device service!")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to service: ${e.message}", e)
        }

        return false
    }

    /**
     * Disconnect from the device service
     */
    fun disconnect() {
        bindCount--
        if (bindCount <= 0) {
            bindCount = 0
            context?.unbindService(serviceConnection)
            isConnected = false
            deviceServiceBinder = null
            printerService = null
            Log.d(TAG, "Disconnected from service")
        }
    }

    /**
     * Get the printer service interface
     */
    fun getPrinterService(): IPrinter? {
        return if (isConnected) printerService else null
    }

    /**
     * Check if connected to service
     */
    fun isServiceConnected(): Boolean {
        return isConnected && printerService != null
    }

    /**
     * Call getPrinter() on IDeviceService using binder transaction
     */
    private fun getPrinterFromDeviceService(serviceBinder: IBinder): IBinder? {
        return try {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)
                val result = serviceBinder.transact(TRANSACTION_GET_PRINTER, data, reply, 0)
                if (result) {
                    reply.readException()
                    return reply.readStrongBinder()
                }
            } finally {
                reply.recycle()
                data.recycle()
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting printer from device service: ${e.message}", e)
            null
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "Device service connected! Obtaining printer interface...")
            deviceServiceBinder = service

            try {
                // Get the printer binder from device service using transaction
                val printerBinder = getPrinterFromDeviceService(service)

                if (printerBinder != null) {
                    // Convert printer binder to IPrinter interface
                    printerService = IPrinter.Stub.asInterface(printerBinder)
                    isConnected = true
                    connectionResult = true
                    Log.d(TAG, "Printer interface obtained successfully!")
                } else {
                    Log.e(TAG, "Failed to get printer binder from device service")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error obtaining printer interface: ${e.message}", e)
            }
            connectionLatch?.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(TAG, "Service disconnected!")
            isConnected = false
            deviceServiceBinder = null
            printerService = null
        }
    }
}
