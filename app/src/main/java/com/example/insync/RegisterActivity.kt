package com.example.insync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.insync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    // The following function is used to register the user
    fun registerUser(user: User){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener{
            task->
            if(task.isSuccessful){

                val userFromFirebase: FirebaseUser = task.result!!.user!!
                val insyncUser = User(userFromFirebase.uid, user.email, user.name, user.student)

                // hashmap of values to be added
                val dataMap = hashMapOf<String, Any>(
                    "uid" to insyncUser.uid,
                    "name" to insyncUser.name,
                    "email" to insyncUser.email,
                    "student" to insyncUser.student
                )
                // uploading data to database
                FirebaseFirestore.getInstance().collection("users").add(dataMap).addOnCompleteListener {
                    task_two->
                    if(task_two.isSuccessful){
                        Log.i("FIREBASE :", "DATA UPLOADED FOR ${insyncUser.uid} | ${insyncUser.email}")
                        // TODO: Navigate to homepage & pass a new insyncUser object
                    }else{
                        Log.d("FIREBASE :", "Failed registering for ${user.email}")
                        // TODO: Toast Failed
                    }
                }

            }else{
                Log.d("FIREBASE :", "Failed registering for ${user.email}")
                // TODO: Toast Failed
            }
        }
    }

}