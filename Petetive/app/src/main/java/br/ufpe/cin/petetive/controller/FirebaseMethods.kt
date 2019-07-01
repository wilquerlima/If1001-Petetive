package br.ufpe.cin.petetive.controller

import br.ufpe.cin.petetive.data.Pet
import br.ufpe.cin.petetive.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

object FirebaseMethods {

    val database = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef
    val userRef = database.getReference("users")
    val petRef = database.getReference("pet")

    fun getPets(requestCallback: RequestCallback) {
        FirebaseMethods.petRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                requestCallback.onError("Error petRef")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val petList: MutableList<Pet> = ArrayList()
                for (pet in p0.children) {
                    petList.add(pet.getValue(Pet::class.java)!!)
                }
                requestCallback.onSuccess(petList)
            }
        })
    }

    fun getUser(uid: String, requestCallback: RequestCallback) {
        FirebaseMethods.userRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                requestCallback.onError("")
            }

            override fun onDataChange(p0: DataSnapshot) {
                requestCallback.onSuccess(p0.getValue(User::class.java)!!)
            }

        })
    }

    fun atualizarUser(user: User, requestCallback: RequestCallback) {
        val currentUser = mAuth.currentUser
        FirebaseMethods.userRef.child(currentUser!!.uid).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                requestCallback.onSuccess(it)
            }else{
                requestCallback.onError("Error atualizar")
            }
        }
    }
}