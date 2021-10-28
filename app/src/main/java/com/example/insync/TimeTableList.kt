package com.example.insync

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.insync.MainActivity.Companion.gUser
import java.text.SimpleDateFormat
import java.util.*


class TimeTableList : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var myRecyclerAdapter: MyRecyclerAdapter

    lateinit var s1: MutableList<String>
    lateinit var s2: MutableList<String>
    lateinit var urlLinks: MutableList<String>

    var images: Array<Int> = arrayOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_list)

        recyclerView = findViewById(R.id.recyclerView)


        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek: String = sdf.format(d)

        val scheduleIntent = intent
        val scheduleDay: String? = scheduleIntent.getStringExtra("daySelected")
        Toast.makeText(this, scheduleDay + " " + dayOfTheWeek, Toast.LENGTH_SHORT).show()

//        if (scheduleDay != null) {
//            retrieveDataForTeacher(gUser, scheduleDay)
//        } else {
//            retrieveDataForTeacher(gUser, dayOfTheWeek)
//        }

        s1 = resources.getStringArray(R.array.subject_name).toMutableList()
        s2 = resources.getStringArray(R.array.lecture_timing).toMutableList()
        urlLinks = mutableListOf()
        for (i in 0..5) {
            urlLinks.add("https://www.google.com/")
        }

        myRecyclerAdapter =
            MyRecyclerAdapter(applicationContext, s1, s2, images)
        recyclerView.adapter = myRecyclerAdapter
        myRecyclerAdapter.setOnItemClickListener(object : MyRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, s1[position], Toast.LENGTH_SHORT).show()
                val url = urlLinks[position]
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        })
        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topmenu, menu)
        return true
    }

    fun CreateNewEvent(view: android.view.View) {


        val intent = Intent(applicationContext, AddEventActivity::class.java)
        val prev_intent = getIntent()
        var insyncUserArray = prev_intent.getStringArrayListExtra("insyncUser")!!

        intent.putStringArrayListExtra("insyncUser", insyncUserArray)
        //for debugging
        Toast.makeText(this, insyncUserArray[1], Toast.LENGTH_SHORT).show()

        startActivity(intent)
    }

    // function to retrieve data for teacher
    fun retrieveDataForTeacher(insyncUser: User, day: String) {

        val dayToday = day
        Log.d("WEEKDAY:", dayToday)
        var teacherDataForToday =
            FirebaseFirestore.getInstance().collection("teacherDB").document(insyncUser.uid)
                .collection("weekdays").document(dayToday).collection("events").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result!!
                        Log.d("EVENT :", "${result.size()}")
                        if(result.size() != 0){
                            displayReceivedData(result)
                        }

                    } else {

                    }
                }
    }

    private fun displayReceivedData(data: QuerySnapshot) {
        val eventArray = arrayListOf<Event>()
        var x:Int = 0
        Log.d("EVENT :", "${data.size()}")
        for (i in data) {
            eventArray.add(Event(i))
            eventArray[x].printDet()
            x++
        }
        s1.clear()
        s2.clear()
        urlLinks.clear()
        var i:Int = 0
        while (i < x){
            s1.add(eventArray[i].name)
            s2.add(eventArray[i].startAt)
            urlLinks.add(eventArray[i].lectureLink)
            i++
        }
//        for (i in 0..eventArray.size) {
//            s1.add(eventArray[i].name)
//            s2.add(eventArray[i].startAt)
//            urlLinks.add(eventArray[i].lectureLink)
//        }
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
        val intent = Intent(applicationContext, AccountInfo::class.java)
        startActivity(intent)
    }

    fun logoutIntentFun(item: android.view.MenuItem) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

}