package br.ufpe.cin.petetive.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val nome: String? = "",
    val email: String? = "",
    val telefone: String? = ""
)