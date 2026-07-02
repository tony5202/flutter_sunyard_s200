package com.philaphonh.flutter_sunyard_i80

import com.socsi.smartposapi.terminal.TerminalManager

/** A module to retrieve terminal information */
class TerminalInfoModule {
    /**
     * An instance of [TerminalManager].
     * */
    private var terminalInstance: TerminalManager = TerminalManager.getInstance()

    /** Getter string of [serialNumber]. */
    public val snGetterString: String = "serialNumber"

    /** Terminal serial number. */
    public val serialNumber: String = terminalInstance.sn
}