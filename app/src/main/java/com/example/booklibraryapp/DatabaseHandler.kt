package com.example.booklibraryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

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

    fun addBook(book: BookModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(BOOK_ID, book.bookID)
            put(BOOK_NAME, book.bookName)
            put(BOOK_AUTHOR_NAME, book.bookAuthor)
        }
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    fun viewBooks(): List<BookModelClass> {
        val bookList = mutableListOf<BookModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
            val bookIdIndex = cursor.getColumnIndex(BOOK_ID)
            val bookNameIndex = cursor.getColumnIndex(BOOK_NAME)
            val bookAuthorIndex = cursor.getColumnIndex(BOOK_AUTHOR_NAME)

            if (bookIdIndex >= 0 && bookNameIndex >= 0 && bookAuthorIndex >= 0) {
                // The column indices are valid, proceed with retrieving data
                if (cursor.moveToFirst()) {
                    do {
                        val bookId = cursor.getString(bookIdIndex)
                        val bookName = cursor.getString(bookNameIndex)
                        val bookAuthor = cursor.getString(bookAuthorIndex)
                        val book = BookModelClass(bookId, bookName, bookAuthor)
                        bookList.add(book)
                    } while (cursor.moveToNext())
                }
            } else {
                // Handle the case where one or more column indices are invalid
                // Log an error, throw an exception, or handle it based on your app's requirements
                // For now, return an empty list
                return emptyList()
            }
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return emptyList()
        } finally {
            cursor?.close() // Close the cursor regardless of whether it's null or not
            db.close() // Close the database
        }
        return bookList
    }

    fun updateBooks(book: BookModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(BOOK_NAME, book.bookName)
            put(BOOK_AUTHOR_NAME, book.bookAuthor)
        }
        val success = db.update(TABLE_CONTACTS, contentValues, "$BOOK_ID = ?", arrayOf(book.bookID.toString()))
        db.close()
        return success
    }

    fun deleteBooks(book: BookModelClass): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_CONTACTS, "$BOOK_ID = ?", arrayOf(book.bookID.toString()))
        db.close()
        return success
    }

}