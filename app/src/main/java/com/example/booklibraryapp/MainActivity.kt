package com.example.booklibraryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var bookIdEditText: EditText
    private lateinit var bookNameEditText: EditText
    private lateinit var bookAuthorNameText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookIdEditText = findViewById(R.id.book_id)
        bookNameEditText = findViewById(R.id.book_name)
        bookAuthorNameText = findViewById(R.id.book_author)
    }

    //method to save records in database
    fun saveRecord(view: View){
        val id = bookIdEditText.text.toString()
        val bookName = bookNameEditText.text.toString()
        val authorName = bookAuthorNameText.text.toString()
        //val databaseHandler: DatabaseHandler = DatabaseHandler(this)
    }
}