package com.example.insync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.firebase.firestore.FirebaseFirestore

class AddEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
    }

    fun createEvent(insyncUser: User, classEvent: Event){
        val dataMap = hashMapOf<String, Any>(
            "name" to classEvent.name,
            "classCode" to classEvent.classRoomCode,
            "startAt" to classEvent.startAt,
            "endAt" to classEvent.endAt,
            "weekday" to classEvent.weekday,
            "owner" to classEvent.owner,
            "link" to classEvent.lectureLink,
            "desc" to classEvent.description
        )
        // Adding info to teacher's personal DB
        FirebaseFirestore.getInstance().collection("teacherDB").document(insyncUser.uid).
        collection("weekdays").document(classEvent.weekday).collection("events").add(dataMap)
            .addOnCompleteListener {
                task->
                if(task.isSuccessful){

                    // Adding info to public DB
                    FirebaseFirestore.getInstance().collection("classroomDB")
                        .document(classEvent.classRoomCode).collection("weekdays").document(classEvent.weekday)
                        .collection("events").add(dataMap).addOnCompleteListener {
                            task_two->
                            if(task_two.isSuccessful){
                                // TODO: Toast Successful, redirect to homepage
                            }
                            else{
                                // TODO: Toast Failed
                            }
                        }

                }else{
                    // TODO: Toast Failed
                }
            }
    }

}