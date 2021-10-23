package com.example.insync

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerAdapter(
    ct: Context,
    s1: Array<String>,
    s2: Array<String>,
    img: Array<Int>
) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {

    var data1 = s1
    var data2 = s2
    var images = img
    var context = ct

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myText1: TextView = itemView.findViewById(R.id.subject_name)
        var myText2: TextView = itemView.findViewById(R.id.lecture_timings)
        var lectureImage: ImageView = itemView.findViewById(R.id.subject_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.myText1.setText(data1[position])
        holder.myText2.setText(data2[position])
        holder.lectureImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return data1.size
    }
}