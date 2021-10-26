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

    lateinit var insyncUser: User
    lateinit var myEmail: EditText
    lateinit var myPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myEmail = findViewById(R.id.emailEditText)
        myPassword = findViewById(R.id.passwordEditText)

        redirectUserToHomePage()

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
                    var insyncUserArray = arrayListOf<String>() ;
                    insyncUserArray.add(insyncUser.uid)
                    insyncUserArray.add(insyncUser.email)
                    insyncUserArray.add(insyncUser.name)
                    insyncUserArray.add(insyncUser.student.toString())
                    Log.i("FIREBASE :", "Retrieved data for ${insyncUser.email}")
                    // TODO: Redirect to home page & pass insyncUser object
                    val intent: Intent = Intent(applicationContext, TimeTableList::class.java)
                    intent.putExtra("insyncUser", insyncUserArray)
                    startActivity(intent)
                    Toast.makeText(this, "Intent to next activity", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("FIREBASE :", "FAILED FOR $userID")
                    // TODO: Toast unable to retrieve data
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
            // TODO: Toast login please
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
                    // TODO: toast failed
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()



                }
            }
    }

    fun redirectIt(view: android.view.View) {
        if (myEmail.text.toString() != null && myPassword.text.toString() != null) {
            loginWithEmailAndPassword(myEmail.text.toString(), myPassword.text.toString())
        } else {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        }
    }
}