package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.insync.model.Event
import com.example.insync.model.User
import com.google.firebase.firestore.FirebaseFirestore


class AddEventActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var Weekdays: Spinner
    lateinit var LectureTime:TimePicker
    lateinit var RepeatLecture:CheckBox
    lateinit var SubjectName:EditText
    lateinit var LectureLink:EditText
    lateinit var ClassroomCode:EditText
    lateinit var Description:EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        Weekdays = findViewById(R.id.Weekdays)
        LectureTime = findViewById(R.id.LectureTiming)
        RepeatLecture = findViewById(R.id.checkBox)
        SubjectName = findViewById(R.id.SubjectName)
        LectureLink = findViewById(R.id.LectureLink)
        ClassroomCode = findViewById(R.id.ClassCode)
        Description = findViewById(R.id.Description)

        SubjectName.text.clear()
        LectureLink.text.clear()
        ClassroomCode.text.clear()
        Description.text.clear()


        val teachersIntent = intent
        if(teachersIntent.getStringExtra("SubjectName")!=null){
            SubjectName.setText(teachersIntent.getStringExtra("SubjectName"))
            LectureLink.setText(teachersIntent.getStringExtra("LectureLink"))
        }



    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }
    override fun onNothingSelected(parent:AdapterView<*>?)
    {

    }

    fun createEvent(insyncUser: User, classEvent: Event){
        val dataMap = hashMapOf<String, Any>(
            "name" to classEvent.name,
            "classcode" to classEvent.classRoomCode,
            "startAt" to classEvent.startAt,
            "endAt" to classEvent.endAt,
            "weekday" to classEvent.weekday,
            "owner" to classEvent.owner,
            "link" to classEvent.lectureLink,
            "desc" to classEvent.description
        )

        // Adding info to teacher's personal DB
        FirebaseFirestore.getInstance().collection("teacherDB").document(insyncUser.uid).
        collection("weekdays").document(classEvent.weekday).collection("events").document("${classEvent.classRoomCode}_${classEvent.name}").set(dataMap)
            .addOnCompleteListener {
                task->
                if(task.isSuccessful){

                    // Adding info to public DB
                    FirebaseFirestore.getInstance().collection("classroomDB")
                        .document(classEvent.classRoomCode).collection("weekdays").document(classEvent.weekday)
                        .collection("events").document("${classEvent.classRoomCode}_${classEvent.name}").set(dataMap).addOnCompleteListener {
                            task_two->
                            if(task_two.isSuccessful){

                                val next_intent = Intent(applicationContext, TimeTableList::class.java)
                                startActivity(next_intent)
                                Toast.makeText(this, "Lecture Added!", Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                Toast.makeText(this,"Task1 Successfull: Task 2 Unsucessful",Toast.LENGTH_SHORT).show()
                            }
                        }

                }else{

                    Toast.makeText(this,"Failed Task1: Failed Task2 ",Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun check_input(): String
    {   var Error = ""

        if(SubjectName.text.toString().isEmpty())
        {
            Error += "Please Enter Subject Name\n"
        }
        if(ClassroomCode.text.toString().isEmpty())
        {
            Error += "Please Enter Classroom code\n"
        }
        if(LectureLink.text.toString().isEmpty())
        {
            Error += "Please Enter Lecture Link\n"
        }
        if(Description.text.toString().isEmpty())
        {
            Error += "Please Enter Description \n"
        }
        return Error
    }
    fun on_confirm(view: View) {


        var DisplayError:String = check_input()
        if(DisplayError.isEmpty()) {
            //Event Object Code
            val prev_intent: Intent = getIntent()

            var insyncUserArray = prev_intent.getStringArrayListExtra("insyncUser")!!

            var StartTime: String =
                LectureTime.getHour().toString() + ":" + LectureTime.getMinute().toString()
            var EndTime: String =
                (LectureTime.getHour() + 1).toString() + ":" + LectureTime.getMinute()
                    .toString()
            var Owner: String = insyncUserArray[0]

            var NewEvent: Event = Event(
                SubjectName.text.toString(),
                ClassroomCode.text.toString(),
                LectureLink.text.toString(),
                Weekdays.selectedItem.toString(),
                StartTime,
                EndTime,
                Description.text.toString(),
                Owner
            )
            var insyncUser: User = User(
                insyncUserArray[0],
                insyncUserArray[1],
                insyncUserArray[2],
                insyncUserArray[3].toBoolean()
            )
            createEvent(insyncUser, NewEvent)
            //for debugging
            Toast.makeText(this, StartTime+" "+EndTime, Toast.LENGTH_SHORT).show()
        }

        else
        {
            Toast.makeText(this, DisplayError, Toast.LENGTH_SHORT).show()

        }

    }

}