package com.example.booklibraryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast


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
        //Retrieve input values from EditText fields
        val id = bookIdEditText.text.toString()
        val bookName = bookNameEditText.text.toString()
        val authorName = bookAuthorNameText.text.toString()

        //Create an instance of the DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        //Check if input fields are not empty
        if(id.trim() != "" && bookName.trim() != "" && authorName.trim() != ""){
            //call addBook method
            val status = databaseHandler.addBook(BookModelClass(id,bookName,authorName))
            //If record is added successfully, show a toast message
            if(status > -1){
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                //Clear EditText fields
                bookIdEditText.text.clear()
                bookNameEditText.text.clear()
                bookAuthorNameText.text.clear()
            }else{
                // If any field is empty, show an error message
                Toast.makeText(applicationContext, "ID, name, or email cannot be blank", Toast.LENGTH_LONG).show()
            }
        }

    }
}