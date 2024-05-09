package com.example.booklibraryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.content.DialogInterface
import android.widget.ListView
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private lateinit var bookIdEditText: EditText
    private lateinit var bookNameEditText: EditText
    private lateinit var bookAuthorNameText:EditText
    private lateinit var listView: ListView
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
    // Method for reading records from the database and displaying in ListView
    fun viewRecord(view: View) {
        // Create an instance of the DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        // Call viewEmployee method to retrieve records from the database
        val book: List<BookModelClass> = databaseHandler.viewBooks()

        // Initialize arrays to store record data
        val bookArrayBookId = Array<String>(book.size) { "0" }
        val bookArrayBookName = Array<String>(book.size) { "null" }
        val bookArrayAuthorName = Array<String>(book.size) { "null" }
        var index = 0
        // Loop through each record and populate arrays
        for (e in book) {
            bookArrayBookId[index] = e.bookID
            bookArrayBookName[index] = e.bookName
            bookArrayAuthorName[index] = e.bookAuthor
            index++
        }
        // Create custom ArrayAdapter to display records in ListView
        val myListAdapter = MyListAdapter(this, bookArrayBookId, bookArrayBookName, bookArrayAuthorName)
        listView.adapter = myListAdapter
    }

    // Method for updating records based on user id
    fun updateRecord(view: View) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialogue, null)
        dialogBuilder.setView(dialogView)

        // Get references to EditText fields in the update dialog layout
        val edtBookId = dialogView.findViewById(R.id.updateBookId) as EditText
        val edtBookName = dialogView.findViewById(R.id.updateBookName) as EditText
        val edtAuthorName = dialogView.findViewById(R.id.updateAuthorName) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateBookId = edtBookId.text.toString()
            val updateBookName = edtBookName.text.toString()
            val updateAuthorName = edtAuthorName.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            // Check if input fields are not empty
            if (updateBookId.trim() != "" && updateBookName.trim() != "" && updateAuthorName.trim() != "") {
                // Call updateEmployee method to update the record in the database
                val status = databaseHandler.updateBooks(BookModelClass(updateBookId, updateBookName, updateAuthorName))
                // If record is updated successfully, show a toast message
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record updated", Toast.LENGTH_LONG).show()
                }
            } else {
                // If any field is empty, show an error message
                Toast.makeText(applicationContext, "ID, name, or email cannot be blank", Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // Do nothing
        })
        val b = dialogBuilder.create()
        b.show()
    }

    // Method for deleting records based on id
    fun deleteRecord(view: View) {
        // Create AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialogue, null)
        dialogBuilder.setView(dialogView)

        // Get reference to EditText field in delete dialog layout
        val dltBookId = dialogView.findViewById(R.id.deleteBookId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter ID below")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltBookId.text.toString()
            // Create an instance of the DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            // Check if input field is not empty
            if (deleteId.trim() != "") {
                // Call deleteEmployee method to delete the record from the database
                val status = databaseHandler.deleteBooks(BookModelClass(deleteId, "",
                    ""))
                // If record is deleted successfully, show a toast message
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record deleted", Toast.LENGTH_LONG).show()
                }
            } else {
                // If field is empty, show an error message
                Toast.makeText(applicationContext, "ID cannot be blank", Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            // Do nothing
        })
        val b = dialogBuilder.create()
        b.show()
    }
}

