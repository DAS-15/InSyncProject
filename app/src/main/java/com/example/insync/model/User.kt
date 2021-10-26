package com.example.insync.model

class User {
    var uid:String = "NONE"
    var email:String = "NONE"
    var name:String = "NONE"
    var student:Boolean = true
    var classRoomCode:String = "NONE"
    var batchCode:String = "NONE"
    var password:String = "NONE"
    var profileComplete:Int = 0;

    // constructor to be used in case of no uid or internal use
    public constructor(email:String){}

    // used for making abstract data for firebase user
    public constructor(uid:String, email:String){
        this.uid = uid
        this.email = email

    }

    public constructor(uid:String, email: String, name:String, student:Boolean){
        this.student = student
        this.uid = uid
        this.name = name
        this.email = email
    }
}