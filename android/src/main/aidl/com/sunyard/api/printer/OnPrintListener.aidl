package com.sunyard.api.printer;

/**
 * Callback interface for print operations
 */
interface OnPrintListener {
    // Called when printing finishes successfully
    void onFinish();

    // Called when printing fails
    // errorCode: error code indicating the failure reason
    void onError(int errorCode);
}
