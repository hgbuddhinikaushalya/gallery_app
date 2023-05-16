package com.example.gallery

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class Items_for_cus : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CusItemAdapter
    private lateinit var mySharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_for_cus)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = FirebaseDatabase.getInstance().reference
        val itemdata = mutableListOf<Item>()
        adapter = CusItemAdapter(itemdata)
        recyclerView.adapter = adapter
        val orderComplete = findViewById<Button>(R.id.completeButton)

        orderComplete.setOnClickListener {
            mySharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE)


            // Retrieve the cart data from shared preferences
            val cartDataString = mySharedPreferences.getString("cartData", "{}")

            var itemcode = generateRandomCode(10)
            val orderId = database.child("order").child(itemcode).key

            // Get the current date and time
            val currentDate = Date()

            // Define the desired format for the date and time

            // Define the desired format for the date and time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            // Format the current date and time using the date format

            // Format the current date and time using the date format
            val formattedDateTime: String = dateFormat.format(currentDate)

            // Create a new order object
            val order = Order(orderId.toString(), cartDataString.toString(), formattedDateTime , "1" ,"1")

            if (order != null) {
                database.child("order").child(orderId.toString()).setValue(order)
            }

            // Show a success message
            Toast.makeText(this, "Order is placed.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Items_for_cus, cart::class.java)
            startActivity(intent)

            val editor = mySharedPreferences.edit()
            editor.remove("cartData")
            editor.apply()
        }

        database.child("item").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemdata.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val reservation = postSnapshot.getValue(Item::class.java)
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

    data class Item(
        val itemId: String = "",
        val itemname: String = "",
        val price: String = "",
        val imagename: String?
    ) {
        constructor() : this("", "", "", "")
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

    fun back(view: View) {
        val intent = Intent(this@Items_for_cus, Dashboard::class.java)
        startActivity(intent)
    }

    fun generateRandomCode(length: Int): String {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = java.util.Random()
        val sb = StringBuilder(length)
        for (i in 0 until length) {
            sb.append(chars[random.nextInt(chars.length)])
        }
        return sb.toString()
    }

}