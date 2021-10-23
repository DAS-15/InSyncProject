package com.example.insync

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class TimeTableList : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    lateinit var s1 : Array<String>
    lateinit var s2 : Array<String>

    var images : Array<Int> = arrayOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher
    )

    // Drawer Layout

    lateinit var drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_list)

        drawerLayout = findViewById(R.id.drawar_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.topAppBar)


        setSupportActionBar(toolbar)

        val toogle : ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawar_open,R.string.navigation_drawar_close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        recyclerView = findViewById(R.id.recyclerView)

        s1 = resources.getStringArray(R.array.subject_name)
        s2 = resources.getStringArray(R.array.lecture_timing)

        val myRecyclerAdapter: MyRecyclerAdapter = MyRecyclerAdapter(this, s1, s2, images)
        recyclerView.adapter = myRecyclerAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }
}