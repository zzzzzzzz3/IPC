// OnNewBookArrivedListener.aidl
package com.example.msi.ipc;
import com.example.msi.ipc.modle.Book;
// Declare any non-default types here with import statements

interface OnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}
