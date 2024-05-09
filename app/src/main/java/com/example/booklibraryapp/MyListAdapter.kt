package com.example.booklibraryapp

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(private val context: Activity, private val bookId: Array<String>, private val bookName: Array<String>, private val authorName: Array<String>)
    : ArrayAdapter<String>(context, R.layout.customer_list, bookName) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.customer_list, parent, false)

        val bookIdText = rowView.findViewById(R.id.textViewBookId) as TextView
        val bookNameText = rowView.findViewById(R.id.textViewBookName) as TextView
        val authorNameText = rowView.findViewById(R.id.textViewAuthorName) as TextView

        bookIdText.text = "Book Id: ${bookId[position]}"
        bookNameText.text = "Book Name: ${bookName[position]}"
        authorNameText.text = "Author Name: ${authorName[position]}"
        return rowView
    }

}