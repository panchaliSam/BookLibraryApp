package com.example.booklibraryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "LibraryDatabase"
        private val TABLE_CONTACTS = "BookTable"
        private val BOOK_ID = "book_id"
        private val BOOK_NAME = "book_name"
        private val BOOK_AUTHOR_NAME = "book_author_name"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            val CREATE_CONTACTS_TABLE = ("CREATE TABLE $TABLE_CONTACTS("
                    + "$BOOK_ID TEXT PRIMARY KEY,"
                    + "$BOOK_NAME TEXT,"
                    + "$BOOK_AUTHOR_NAME TEXT)")
            it.execSQL(CREATE_CONTACTS_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            it.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
            onCreate(it)
        }
    }

    fun viewBooks(): List<BookModelClass> {
        val bookList = mutableListOf<BookModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
            if (cursor != null && cursor.moveToFirst()) {
                val bookIdIndex = cursor.getColumnIndex(BOOK_ID)
                val bookNameIndex = cursor.getColumnIndex(BOOK_NAME)
                val bookAuthorIndex = cursor.getColumnIndex(BOOK_AUTHOR_NAME)

                do {
                    // Check if column indices are valid
                    if (bookIdIndex >= 0 && bookNameIndex >= 0 && bookAuthorIndex >= 0) {
                        val bookId = cursor.getString(bookIdIndex)
                        val bookName = cursor.getString(bookNameIndex)
                        val bookAuthor = cursor.getString(bookAuthorIndex)
                        val book = BookModelClass(bookId, bookName, bookAuthor)
                        bookList.add(book)
                    } else {
                        // Handle the case where one or more column indices are invalid
                        Log.e("DatabaseHandler", "One or more column indices are invalid")
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return bookList
    }


    fun addBook(book: BookModelClass): Long {
        val db = this.writableDatabase
        var success: Long = -1
        try {
            val contentValues = ContentValues().apply {
                put(BOOK_ID, book.bookID)
                put(BOOK_NAME, book.bookName)
                put(BOOK_AUTHOR_NAME, book.bookAuthor)
            }
            success = db.insert(TABLE_CONTACTS, null, contentValues)
        } catch (e: SQLiteException) {
            Log.e("DatabaseHandler", "SQLiteException: ${e.message}")
        } finally {
            db.close()
        }
        return success
    }

    // Other methods...

    fun updateBooks(book: BookModelClass): Int {
        val db = this.writableDatabase
        var success = -1
        try {
            val contentValues = ContentValues().apply {
                put(BOOK_NAME, book.bookName)
                put(BOOK_AUTHOR_NAME, book.bookAuthor)
            }
            success = db.update(TABLE_CONTACTS, contentValues, "$BOOK_ID = ?", arrayOf(book.bookID))
        } catch (e: SQLiteException) {
            Log.e("DatabaseHandler", "SQLiteException: ${e.message}")
        } finally {
            db.close()
        }
        return success
    }

    fun deleteBooks(book: BookModelClass): Int {
        val db = this.writableDatabase
        var success = -1
        try {
            success = db.delete(TABLE_CONTACTS, "$BOOK_ID = ?", arrayOf(book.bookID))
        } catch (e: SQLiteException) {
            Log.e("DatabaseHandler", "SQLiteException: ${e.message}")
        } finally {
            db.close()
        }
        return success
    }
}
