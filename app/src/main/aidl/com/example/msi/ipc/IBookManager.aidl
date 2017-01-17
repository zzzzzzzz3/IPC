// IBookManager.aidl
package com.example.msi.ipc;
import com.example.msi.ipc.modle.Book;
import com.example.msi.ipc.OnNewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(OnNewBookArrivedListener listener);
    void unregisterListener(OnNewBookArrivedListener listener);
}
