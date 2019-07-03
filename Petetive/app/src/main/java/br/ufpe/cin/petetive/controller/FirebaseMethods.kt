package br.ufpe.cin.petetive.controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import br.ufpe.cin.petetive.controller.Session.userLogged
import br.ufpe.cin.petetive.data.Pet
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.activity.LoginActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

object FirebaseMethods {

    val database = FirebaseDatabase.getInstance()
    val mAuth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef
    val userRef = database.getReference("users")
    val petRef = database.getReference("pet")

    fun signOut(context: Activity) {
        mAuth.signOut()
        userLogged = null
        context.finish()
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

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
            if (it.isSuccessful) {
                requestCallback.onSuccess(it)
            } else {
                requestCallback.onError("Error atualizar")
            }
        }
    }

    fun cadastrarPet(
        local: String,
        nome: String,
        descricao: String,
        raca: String,
        perdido: Boolean,
        selectedImage: Uri,
        requestCallback: RequestCallback
    ) {

        val idPet = FirebaseMethods.petRef.push().key
        val ref = FirebaseMethods.storageRef.child("images/$idPet/photo")
        val usersId = FirebaseMethods.mAuth.currentUser?.uid.toString()
        var urlImagePet: String

        val uploadTask = ref.putFile(selectedImage)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener {
            if (it.isSuccessful) {
                urlImagePet = it.result.toString()
                FirebaseMethods.petRef.child(idPet!!).setValue(
                    Pet(
                        urlImagePet,
                        local,
                        nome,
                        descricao,
                        raca,
                        usersId,
                        null,
                        perdido
                    )
                ).addOnCompleteListener {
                    requestCallback.onSuccess("Cadastrado com sucesso")
                }.addOnFailureListener {
                    requestCallback.onError("Erro no cadastro")
                }
            } else {
                requestCallback.onError("Houve um erro, tente novamente!")
            }
        }
    }
}