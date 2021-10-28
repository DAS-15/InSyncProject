package com.example.insync

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val recyclerView : RecyclerView = findViewById(R.id.studentRecyclerView)

        val days = arrayOf<String>("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        val scheduleAdapter: ScheduleAdapter = ScheduleAdapter(this, days)
        recyclerView.adapter = scheduleAdapter
        scheduleAdapter.setOnItemClickListener(object : ScheduleAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, days[position], Toast.LENGTH_SHORT).show()
                if(MainActivity.gUser.student)
                {
                    val i = Intent(applicationContext, TimeTableListStudent::class.java)
                    i.putExtra("daySelected", days[position])
                    startActivity(i)
                }
                else
                {
                    val i = Intent(applicationContext, TimeTableList::class.java)
                    i.putExtra("daySelected", days[position])
                    startActivity(i)
                }

            }
        })
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }
}