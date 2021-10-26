package com.example.insync

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class TimeTableListStudent : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var StudentrecyclerView: RecyclerView

    lateinit var Students1: Array<String>
    lateinit var Students2: Array<String>

    var Studentimages: Array<Int> = arrayOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher
    )

    // Drawer Layout

    lateinit var StudentdrawerLayout: DrawerLayout
    lateinit var StudentnavigationView: NavigationView
    lateinit var Studenttoolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_list_student)

        StudentdrawerLayout = findViewById(R.id.student_drawar_layout)
        StudentnavigationView = findViewById(R.id.student_nav_view)
        Studenttoolbar = findViewById(R.id.studenttopAppBar)

        // Recycler View Setup
        StudentrecyclerView = findViewById(R.id.studentRecyclerView)

        Students1 = resources.getStringArray(R.array.subject_name)
        Students2 = resources.getStringArray(R.array.lecture_timing)

        val StudentmyRecyclerAdapter: MyRecyclerAdapter = MyRecyclerAdapter(this, Students1, Students2, Studentimages)
        StudentrecyclerView.adapter = StudentmyRecyclerAdapter
        StudentmyRecyclerAdapter.setOnItemClickListener(object : MyRecyclerAdapter.onItemClickListener {
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

    fun openNav(view: android.view.View) {
        StudentdrawerLayout.openDrawer(StudentnavigationView)
    }

    // Nav drawar onclick listener

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, item.itemId.toString(), Toast.LENGTH_SHORT).show()
        Log.i("itemid", item.itemId.toString())
        StudentdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}