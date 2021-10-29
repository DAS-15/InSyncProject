package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.insync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity(){

    lateinit var Name: EditText
    lateinit var Email: EditText
    lateinit var Privilege: Spinner
    lateinit var Password: EditText
    lateinit var ConfirmPassword: EditText
    lateinit var ClassroomCode:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Name = findViewById(R.id.UserName)
        Email = findViewById(R.id.Email)
        Privilege = findViewById(R.id.Privilege)
        Password = findViewById(R.id.Password)
        ConfirmPassword = findViewById(R.id.ConfirmPassword)
        ClassroomCode = findViewById(R.id.ClassroomCode)
        ClassroomCode.visibility = View.INVISIBLE

        val option_list = listOf<String>("Teacher","Student")
        val adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,option_list)
        Privilege.adapter = adapter
        Privilege.onItemSelectedListener = object: AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                if(Privilege.getItemAtPosition(position) == "Student")
                {
                    Toast.makeText(this@RegisterActivity, "Teacher Selected!", Toast.LENGTH_SHORT).show()
                    ClassroomCode.visibility = View.VISIBLE
                }
                else
                {
                    ClassroomCode.visibility = View.INVISIBLE
                }
            }
            override fun onNothingSelected(parent:AdapterView<*>?)
            {

            }
        }

//        Name.text.clear()
//        Email.text.clear()
//        Password.text.clear()
//        ConfirmPassword.text.clear()
    }

    // The following function is used to register the user
    fun registerUser(user: User){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener{
            task->
            if(task.isSuccessful){

                val userFromFirebase: FirebaseUser = task.result!!.user!!
                val insyncUser = User(userFromFirebase.uid, user.email, user.name, user.student)
                insyncUser.classRoomCode = user.classRoomCode


                // hashmap of values to be added
                val dataMap = hashMapOf<String, Any>(
                    "uid" to insyncUser.uid,
                    "name" to insyncUser.name,
                    "email" to insyncUser.email,
                    "student" to insyncUser.student,
                    "classcode" to insyncUser.classRoomCode
                )
                // uploading data to database
                FirebaseFirestore.getInstance().collection("users").document(insyncUser.uid).set(dataMap).addOnCompleteListener {
                    task_two->
                    if(task_two.isSuccessful){
                        // adding data to the teacherDB
                        if(!insyncUser.student){
                            FirebaseFirestore.getInstance().collection("teacherDB").document(insyncUser.uid).set(dataMap)
                        }
                        Log.i("FIREBASE :", "DATA UPLOADED FOR ${insyncUser.uid} | ${insyncUser.email}")

                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                        var insyncUserArray = arrayListOf<String?>();
                        insyncUserArray.add(insyncUser.uid)
                        insyncUserArray.add(insyncUser.email)
                        insyncUserArray.add(insyncUser.name)
                        insyncUserArray.add(insyncUser.student.toString())

                        var intent: Intent = Intent(applicationContext,TimeTableList::class.java)
                        intent.putStringArrayListExtra("insyncUser", insyncUserArray)
                        MainActivity.gUser = insyncUser
                        startActivity(intent)
                    }
                    else{
                        Log.d("FIREBASE :", "Failed registering for ${user.email}")

                        Toast.makeText(this, "Registration failed (Task2)", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Log.d("FIREBASE :", "Failed registering for ${user.email}")

                Toast.makeText(this, "Task 1 FAIL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun is_valid_email(Email:String):Boolean
    {    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

         if(Email.isEmpty())
         {
             return false
         }
        else return EMAIL_REGEX.toRegex().matches(Email)
    }

    fun input_check(student:Boolean):String
    {
        var error:String = ""
        if(Name.text.toString().isEmpty())
        {
            error += "Enter a valid name \n"
        }
        if(!is_valid_email(Email.text.toString().trim()))
        {
            error +="Please enter a valid email \n"
        }
        if(ClassroomCode.text.toString().isEmpty())
        {
            if(student)
            {
                error+="Enter Classroom Code \n"
            }
        }
        if(Password.text.toString().isEmpty())
        {
            error+="Enter a valid password \n"
        }
        else if(Password.text.toString() != ConfirmPassword.text.toString())
        {
            error += "Passwords do not match \n"
            Password.text.clear()
            ConfirmPassword.text.clear()
        }
        Toast.makeText(this, Privilege.selectedItem.toString(), Toast.LENGTH_SHORT).show()
        return error

    }

    fun create_account(view: View)
    {
        var student = if(Privilege.selectedItem.toString() == "Student") true else false
        var DisplayError:String = input_check(student)

        if(DisplayError.isEmpty())
        {

            var new_user:User = User("NONE",Email.text.toString(),Name.text.toString(),student)

            new_user.password = Password.text.toString()
            if(student)
            {
                new_user.classRoomCode = ClassroomCode.text.toString()
            }
            registerUser(new_user)
        }
        else
        {
            Toast.makeText(this, DisplayError, Toast.LENGTH_SHORT).show()
        }
    }

}