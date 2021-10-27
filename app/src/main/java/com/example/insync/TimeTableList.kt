package com.example.insync

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class TimeTableList : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    lateinit var s1: Array<String>
    lateinit var s2: Array<String>

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

        s1 = resources.getStringArray(R.array.subject_name)
        s2 = resources.getStringArray(R.array.lecture_timing)

        val myRecyclerAdapter: MyRecyclerAdapter = MyRecyclerAdapter(applicationContext, s1, s2, images)
        recyclerView.adapter = myRecyclerAdapter
        myRecyclerAdapter.setOnItemClickListener(object : MyRecyclerAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, s1[position], Toast.LENGTH_SHORT).show()
                val url = "https://www.google.com"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        })
        recyclerView.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.topmenu,menu)
        return true
    }

    fun CreateNewEvent(view: android.view.View) {


        val intent = Intent(applicationContext, AddEventActivity::class.java)
        val prev_intent = getIntent()
        var insyncUserArray = prev_intent.getStringArrayListExtra("insyncUser")!!

        intent.putStringArrayListExtra("insyncUser",insyncUserArray)
        //for debugging
        Toast.makeText(this, insyncUserArray[1], Toast.LENGTH_SHORT).show()

        startActivity(intent)
    }

    // function to retrieve data for teacher
    fun retrieveDataForTeacher(insyncUser: User, day:String){

        val dayToday = day
        var teacherDataForToday = FirebaseFirestore.getInstance().collection("teacherDB").
        document(insyncUser.uid).collection("weekday").document(dayToday).collection("events").get().addOnCompleteListener {
                task->
            if(task.isSuccessful){
                val result = task.result!!
                displayReceivedData(result)
            }else{

            }
        }
    }

    private fun displayReceivedData(data: QuerySnapshot){
        val eventArray = arrayListOf<Event>()
        for (i in data){
            eventArray.add(Event(i))
        }
        // TODO : Use the eventArray to display the data
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
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

}