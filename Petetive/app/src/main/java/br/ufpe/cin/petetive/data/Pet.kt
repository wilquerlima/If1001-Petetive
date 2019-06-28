package br.ufpe.cin.petetive.data

import android.location.Location
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Pet(
    val urlImage: String = "",
    val local: String = "",
    val nome: String = "",
    val descricao: String = "",
    val raca: String = "",
    val uidUser: String = "",
    val location: Location? = null
)