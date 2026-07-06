package com.philaphonh.flutter_sunyard_s200

import com.socsi.smartposapi.terminal.TerminalManager
import android.util.Log

/** A module to retrieve terminal information */
class TerminalInfoModule {
    init {
        Log.e("S200_TerminalModule", "=== TERMINAL MODULE INITIALIZED ===")
    }

    /**
     * An instance of [TerminalManager].
     * */
    private var terminalInstance: TerminalManager? = null

    /** Getter string of [serialNumber]. */
    public val snGetterString: String = "serialNumber"

    /** Terminal serial number. */
    public val serialNumber: String
        get() {
            // Return placeholder since native libraries are not available
            // Using service-based approach instead of JAR SDK
            return "S200-DEVICE"
        }
}