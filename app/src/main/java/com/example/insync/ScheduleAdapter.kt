package com.example.insync

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(
    ct: Context,
    days: Array<String>
) : RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    val c = ct
    val dayarr = days
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class MyViewHolder(itemView: View, listener: ScheduleAdapter.onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        var dayItem = itemView.findViewById<TextView>(R.id.dayname)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(c)
        val view: View = inflater.inflate(R.layout.schedule_item, parent, false)
        return ScheduleAdapter.MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dayItem.setText(dayarr[position])
    }

    override fun getItemCount(): Int {
        return dayarr.size
    }


}