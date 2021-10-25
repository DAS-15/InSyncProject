package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun redirectIt(view: android.view.View) {
        val intent : Intent = Intent(applicationContext, TimeTableList::class.java)
        startActivity(intent)
    }

    fun redirectUserToHomePage(){
        var userFromFirebase = FirebaseAuth.getInstance().currentUser
        if(userFromFirebase != null){
            val userID:String = userFromFirebase.uid
            // TODO: Redirect to home page & pass uid with it
        }
        else{
            // TODO: Toast login please
        }
    }

    fun loginWithEmailAndPassword(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
            task->
            if(task.isSuccessful){
                val userFromFirebaseAuth:FirebaseUser = task.result!!.user!!
                val userID:String = userFromFirebaseAuth.uid.toString()

                // TODO: Redirect to the home page & pass the uid with it
            }
            else{
                // TODO: toast failed
            }
        }
    }
}