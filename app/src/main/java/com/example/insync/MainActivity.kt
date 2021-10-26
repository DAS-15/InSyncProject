package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.insync.model.User
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun redirectIt(view: android.view.View) {
        val intent : Intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }

    private fun changePageToHome(userFromFirebase:FirebaseUser){
        val userID:String = userFromFirebase.uid
        FirebaseFirestore.getInstance().collection("users").document(userID).get().addOnCompleteListener {
                task_two->
            if(task_two.isSuccessful){
                val userFields = task_two.result!!

                val insyncUser = User(userFromFirebase.uid,userFromFirebase.email!!, userFields["name"] as String,
                    userFields["student"] as Boolean
                )
                Log.i("FIREBASE :", "Retrieved data for ${insyncUser.email}")
                // TODO: Redirect to home page & pass insyncUser object


            }else{
                Log.d("FIREBASE :", "FAILED FOR $userID")
                // TODO: Toast unable to retrieve data
            }
        }
    }

    // The following function redirects the user if it has already logged in
    fun redirectUserToHomePage(){
        var userFromFirebase = FirebaseAuth.getInstance().currentUser
        if(userFromFirebase != null){
            val userID:String = userFromFirebase.uid

            Log.i("FIREBASE : ", "USER ID: $userID AT MainActivity Through Redirecting")
            changePageToHome(userFromFirebase)
        }
        else{
            // TODO: Toast login please
        }
    }


    // The following function performs login provided email, password (String) as input
    fun loginWithEmailAndPassword(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
            task->
            if(task.isSuccessful){
                val userFromFirebase:FirebaseUser = task.result!!.user!!
                val userID:String = userFromFirebase.uid
                Log.i("FIREBASE : ", "USER ID: $userID AT MainActivity Through Login")

                changePageToHome(userFromFirebase)

            }
            else{
                Log.i("FIREBASE : ", "EMAIL : $email | PASSWORD: $password")
                // TODO: toast failed
            }
        }
    }
}