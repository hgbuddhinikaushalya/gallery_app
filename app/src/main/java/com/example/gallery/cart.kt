package com.example.gallery

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class cart : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = FirebaseDatabase.getInstance().reference
        val itemdata = mutableListOf<Order>()
        adapter = CartAdapter(itemdata)
        recyclerView.adapter = adapter




        database.child("cart").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemdata.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val reservation = postSnapshot.getValue(Order::class.java)
                    reservation?.let {
                        itemdata.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(ContentValues.TAG, "Failed to read value.", databaseError.toException())
            }
        })
    }

    data class Order(
        val cartId: String = "",
        val ItemList: String = "",
        val datAndTime: String = "",
        val user: String ="",
        val status: String?
    ) {
        constructor() : this("", "", "", "","")
    }





}