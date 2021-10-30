package com.example.insync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.insync.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        var gUser : User = User("demo@gmail.com")
    }


    lateinit  var insyncUser: User
    lateinit var myEmail: EditText
    lateinit var myPassword: EditText





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectUserToHomePage()
        setContentView(R.layout.activity_main)
        myEmail = findViewById(R.id.emailEditText)
        myPassword = findViewById(R.id.passwordEditText)



    }

    private fun changePageToHome(userFromFirebase: FirebaseUser) {
        val userID: String = userFromFirebase.uid
        FirebaseFirestore.getInstance().collection("users").document(userID).get()
            .addOnCompleteListener { task_two ->
                if (task_two.isSuccessful) {
                    val userFields = task_two.result!!

                    insyncUser = User(
                        userFromFirebase.uid,
                        userFromFirebase.email!!,
                        userFields["name"] as String,
                        userFields["student"] as Boolean
                    )


                    if(userFields["classcode"] != null ){
                        insyncUser.classRoomCode = userFields["classcode"] as String
                    }

                    var insyncUserArray = arrayListOf<String?>();
                    insyncUserArray.add(insyncUser.uid)
                    insyncUserArray.add(insyncUser.email)
                    insyncUserArray.add(insyncUser.name)
                    insyncUserArray.add(insyncUser.student.toString())
                    Log.i("FIREBASE :", "Retrieved data for ${insyncUser.email}")
                    gUser = insyncUser

                    if(insyncUser.student == true) {
                        val intent: Intent = Intent(applicationContext, TimeTableListStudent::class.java)
                        intent.putStringArrayListExtra("insyncUser", insyncUserArray)
                        startActivity(intent)
                    } else {
                        val intent: Intent = Intent(applicationContext, TimeTableList::class.java)
                        intent.putStringArrayListExtra("insyncUser", insyncUserArray)
                        startActivity(intent)
                    }
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("FIREBASE :", "FAILED FOR $userID")

                    Toast.makeText(
                        this,
                        "Unable to retrieve data\nPlease try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // The following function redirects the user if it has already logged in
    fun redirectUserToHomePage() {
        var userFromFirebase = FirebaseAuth.getInstance().currentUser
        if (userFromFirebase != null) {
            val userID: String = userFromFirebase.uid

            Log.i("FIREBASE : ", "USER ID: $userID AT MainActivity Through Redirecting")
            changePageToHome(userFromFirebase)

        } else {

            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show()
        }
    }


    // The following function performs login provided email, password (String) as input
    fun loginWithEmailAndPassword(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFromFirebase: FirebaseUser = task.result!!.user!!
                    val userID: String = userFromFirebase.uid
                    Log.i("FIREBASE : ", "USER ID: $userID AT MainActivity Through Login")
                    changePageToHome(userFromFirebase)
                } else {
                    Log.i("FIREBASE : ", "EMAIL : $email | PASSWORD: $password")

                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()


                }
            }
    }
    fun check_input():String
    {
        var error = ""
        if(myEmail.text.toString().isEmpty())
        {
            error+= "Please Enter Email \n"
        }
        if(myPassword.text.toString().isEmpty())
        {
            error+= "Please Enter Password \n"
        }
        return error
    }

    fun redirectIt(view: android.view.View) {
        var Error:String = check_input()
        if(Error.isEmpty()) {

            if (myEmail.text.toString() != null && myPassword.text.toString() != null) {
                loginWithEmailAndPassword(myEmail.text.toString(), myPassword.text.toString())
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, Error, Toast.LENGTH_SHORT).show()

        }
    }

    fun open_registration(view: View)
    {
        var intent: Intent = Intent(applicationContext,RegisterActivity::class.java)
        startActivity(intent)
    }


}