package com.example.insync

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class TimeTableList : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

    // Drawer Layout

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_list)

        drawerLayout = findViewById(R.id.student_drawar_layout)
        navigationView = findViewById(R.id.student_nav_view)
        toolbar = findViewById(R.id.studenttopAppBar)

        // Recycler View Setup
        recyclerView = findViewById(R.id.studentRecyclerView)

        s1 = resources.getStringArray(R.array.subject_name)
        s2 = resources.getStringArray(R.array.lecture_timing)

        val myRecyclerAdapter: MyRecyclerAdapter = MyRecyclerAdapter(this, s1, s2, images)
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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

    fun CreateNewEvent(view: android.view.View) {
        val intent = Intent(applicationContext, AddEventActivity::class.java)
        startActivity(intent)
    }

    fun openNav(view: android.view.View) {
        drawerLayout.openDrawer(navigationView)
    }

    // Nav drawar onclick listener

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, item.itemId.toString(), Toast.LENGTH_SHORT).show()
        Log.i("itemid", item.itemId.toString())
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // function to retrieve data for student
    fun retrieveDataForStudent(insyncUser: User, day: String){

        val dayToday = day // to be retrieved using date time
        var studentDataForToday = FirebaseFirestore.getInstance().collection("classroomDB").
        document(insyncUser.classRoomCode).collection("weekday").document(dayToday).collection("events").get().addOnCompleteListener{
            task->
            if(task.isSuccessful){
                var result = task.result!!
                displayReceivedData(result)
            }else{

            }
        }
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

    private fun displayReceivedData(data:QuerySnapshot){
        val eventArray = arrayListOf<Event>()
        for (i in data){
            eventArray.add(Event(i))
        }
        // TODO : Use the eventArray to display the data
    }

}