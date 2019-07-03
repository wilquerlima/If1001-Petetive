package br.ufpe.cin.petetive.data

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    val nome: String? = "",
    val email: String? = "",
    val telefone: String? = "",
    val idAvatar: String? = ""
) : Serializable