package com.example.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(private val item: List<cart.Order>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_one_cart_item, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cart = item[position]
        val reservationViewHolder = holder as ReservationViewHolder
        reservationViewHolder.bind(cart)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lbl_orderid: TextView = itemView.findViewById(R.id.lbl_order_id)
        private val label_timestamp: TextView = itemView.findViewById(R.id.lbl_placed_date)

        private val context = itemView.context


        fun bind(cart_item: cart.Order) {
            lbl_orderid.text = cart_item.cartId
            label_timestamp.text = cart_item.datAndTime



        }
    }
}
