package com.example.insync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat


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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}