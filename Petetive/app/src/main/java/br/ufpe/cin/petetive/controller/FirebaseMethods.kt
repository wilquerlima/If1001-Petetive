package br.ufpe.cin.petetive.controller

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseMethods {

    val database = FirebaseDatabase.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images")
    val userRef = database.getReference("users")
    val petRef = database.getReference("pet")
}