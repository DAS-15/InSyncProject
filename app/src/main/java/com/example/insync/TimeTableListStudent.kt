package com.example.insync

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insync.MainActivity.Companion.gUser
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class TimeTableListStudent : AppCompatActivity() {
    lateinit var StudentrecyclerView: RecyclerView

    lateinit var Students1: MutableList<String>
    lateinit var Students2: MutableList<String>
    lateinit var StudenturlLinks: MutableList<String>

    var Studentimages: Array<Int> = arrayOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_list_student)

        // Recycler View Setup
        StudentrecyclerView = findViewById(R.id.studentRecyclerView)

        Students1 = resources.getStringArray(R.array.subject_name).toMutableList()
        Students2 = resources.getStringArray(R.array.lecture_timing).toMutableList()
        StudenturlLinks = mutableListOf()
        for (i in 0..5) {
            StudenturlLinks.add("https://www.google.com/")
        }

        retrieveDataForStudent(gUser, "Monday")

        val StudentmyRecyclerAdapter: MyRecyclerAdapter =
            MyRecyclerAdapter(this, Students1, Students2, Studentimages)
        StudentrecyclerView.adapter = StudentmyRecyclerAdapter
        StudentmyRecyclerAdapter.setOnItemClickListener(object :
            MyRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, Students1[position], Toast.LENGTH_SHORT).show()
                val url = "https://www.google.com"
                val urli = Intent(Intent.ACTION_VIEW)
                urli.data = Uri.parse(url)
                startActivity(urli)
            }
        })
        StudentrecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    // function to retrieve data for student
    fun retrieveDataForStudent(insyncUser: User, day: String) {

        val dayToday = day // to be retrieved using date time
        var studentDataForToday = FirebaseFirestore.getInstance().collection("classroomDB")
            .document(insyncUser.classRoomCode).collection("weekdays").document(dayToday)
            .collection("events").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var result = task.result!!
                    if(result.size() != 0){
                        displayReceivedData(result)
                    }

                } else {

                }
            }
    }

    private fun displayReceivedData(data: QuerySnapshot) {
        val eventArray = arrayListOf<Event>()
        for (i in data) {
            eventArray.add(Event(i))
        }
        Students1.clear()
        Students2.clear()
        StudenturlLinks.clear()
        for (i in 0..5) {
            Students1[i] = eventArray[i].name
            Students2[i] = eventArray[i].startAt
            StudenturlLinks[i] = eventArray[i].lectureLink
        }
    }

    fun homeIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }

    fun scheduleIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, ScheduleActivity::class.java)
        startActivity(intent)
    }

    fun accountIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }

    fun logoutIntentFun(item: android.view.MenuItem) {


        FirebaseAuth.getInstance().signOut()
        val intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }
}