package com.example.gallery

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.Editable
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

import org.json.JSONArray
import org.json.JSONObject

class Add_to_cart : AppCompatActivity() {
    private lateinit var btnSave: Button
    val cartArray = JSONArray()

    private lateinit var mySharedPreferences: SharedPreferences


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)


        // display the item code on the screen
        val itemCodeEditText = findViewById<EditText>(R.id.txtCartItemID)
        itemCodeEditText.text = Editable.Factory.getInstance().newEditable(Global_variable.cartItemId)


        // get the item ID from the intent extra
        val itemId = intent.getIntExtra("txtReviewPostId", -1)
        val itemPrice = intent.getIntExtra("txtQty", -1)

        // get a reference to the Add to Cart button
        val addToCartButton = findViewById<Button>(R.id.btnAddCart)
        val close_buttton = findViewById<Button>(R.id.btnClose)


        close_buttton.setOnClickListener {
            val intent = Intent(this@Add_to_cart, Items_for_cus::class.java)
            startActivity(intent)
        }

        // initialize shared preferences
        mySharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE)



        // set an OnClickListener on the Add to Cart button
        addToCartButton.setOnClickListener {

            // get the current cart data from shared preferences
            val cartDataString = mySharedPreferences.getString("cartData", "{}")
            val cartData = JSONObject(cartDataString)

            // get the item quantity from the quantity EditText
            val quantityEditText = findViewById<EditText>(R.id.txtQty)
            val quantityString = quantityEditText.text.toString()
            val quantity = if (quantityString.isNotEmpty()) quantityString.toInt() else 1

            // create a JSON object for the new cart item
            val cartItem = JSONObject()
            cartItem.put("item_name", itemId)
            cartItem.put("item_price", itemPrice)
            cartItem.put("item_quantity", quantity)

            // add the new item to the cart data
            val cartArray = cartData.optJSONArray("cart_items") ?: JSONArray()
            cartArray.put(cartItem)
            cartData.put("cart_items", cartArray)

            // save the updated cart data to shared preferences
            val editor = mySharedPreferences.edit()
            editor.putString("cartData", cartData.toString())
            editor.apply()

            // show a toast message when the button is clicked
            Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@Add_to_cart, Items_for_cus::class.java)
            startActivity(intent)
        }


    }
}