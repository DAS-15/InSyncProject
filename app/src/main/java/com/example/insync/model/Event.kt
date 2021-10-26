package com.example.insync.model

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class Event {
    var name:String = "NONE"
    var classRoomCode:String = "NONE"
    var lectureLink:String = "NONE"
    var weekday:String = "MONDAY"
    var startAt:String = "00:00"
    var endAt:String = "00:40"
    var description:String = "NONE"
    var owner:String = "NONE"

    public constructor(){}
    public constructor(data:DocumentSnapshot){

        this.name = data["name"] as String
        this.classRoomCode = data["classcode"] as String
        this.lectureLink = data["link"] as String
        this.weekday = data["weekday"] as String
        this.startAt = data["startAt"] as String
        this.endAt = data["endAt"] as String
        this.description = data["desc"] as String
        this.owner = data["owner"] as String


    }
}