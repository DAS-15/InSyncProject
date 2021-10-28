package com.example.insync

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insync.MainActivity.Companion.gUser
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*

class TimeTableListStudent : AppCompatActivity() {
    lateinit var StudentrecyclerView: RecyclerView
    lateinit var StudentmyRecyclerAdapter: MyRecyclerAdapter

    lateinit var Students1: MutableList<String>
    lateinit var Students2: MutableList<String>
    lateinit var StudenturlLinks: MutableList<String>
    var eArray: ArrayList<Event> = arrayListOf<Event>()

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

        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek: String = sdf.format(d)

        val scheduleIntent = intent
        val scheduleDay: String? = scheduleIntent.getStringExtra("daySelected")
        Toast.makeText(this, scheduleDay + " " + dayOfTheWeek, Toast.LENGTH_SHORT).show()

        Students1 = resources.getStringArray(R.array.subject_name).toMutableList()
        Students2 = resources.getStringArray(R.array.lecture_timing).toMutableList()
        StudenturlLinks = mutableListOf()
        for (i in 0..5) {
            StudenturlLinks.add("https://www.google.com/")
        }

//        if (scheduleDay != null) {
//            retrieveDataForStudent(gUser, scheduleDay)
//        } else {
//            retrieveDataForStudent(gUser, dayOfTheWeek)
//        }

        retrieveDataForStudent(gUser, "Wednesday")

        StudentmyRecyclerAdapter =
            MyRecyclerAdapter(this, Students1, Students2, Studentimages)
        StudentrecyclerView.adapter = StudentmyRecyclerAdapter
        StudentmyRecyclerAdapter.setOnItemClickListener(object :
            MyRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, Students1[position], Toast.LENGTH_SHORT).show()
                val url = StudenturlLinks[position]
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
                    if (result.size() != 0) {
                        displayReceivedData(result)
                    }

                } else {

                }
            }
    }

    private fun displayReceivedData(data: QuerySnapshot) {
        val eventArray = arrayListOf<Event>()
        var x: Int = 0
        for (i in data) {
            eventArray.add(Event(i))
            eventArray[x].printDet()
            x++
        }
        Students1.clear()
        Students2.clear()
        StudenturlLinks.clear()
        var i: Int = 0
        while (i < x) {
            Students1.add(eventArray[i].name)
            Students2.add(eventArray[i].startAt)
            StudenturlLinks.add(eventArray[i].lectureLink)
            i++
        }
    }

    fun homeIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, TimeTableListStudent::class.java)
        startActivity(intent)
    }

    fun scheduleIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, ScheduleActivity::class.java)
        startActivity(intent)
    }

    fun accountIntentFun(item: android.view.MenuItem) {
        val intent = Intent(applicationContext, AccountInfo::class.java)
        startActivity(intent)
    }

    fun AlertDialog.makeButtonTextBlue() {
        this.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.Red))
        this.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(context, R.color.Green))
    }

    fun logoutIntentFun(item: android.view.MenuItem) {
        MaterialAlertDialogBuilder(this)
            .setMessage("Do you want to logout?")
            .setPositiveButton("NO") { dialog, which ->
                Toast.makeText(this, "Welcome Back!!!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("YES") { dialog, which ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            .show().makeButtonTextBlue()
    }
}