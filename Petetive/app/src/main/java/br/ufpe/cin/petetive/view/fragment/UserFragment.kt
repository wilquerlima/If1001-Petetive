package br.ufpe.cin.petetive.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.controller.ProfileDialog
import br.ufpe.cin.petetive.controller.RecyclerImagesAdapter
import br.ufpe.cin.petetive.controller.RequestCallback
import br.ufpe.cin.petetive.controller.Session.userLogged
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.activity.HomeActivity
import kotlinx.android.synthetic.main.user_fragment.*
import kotlinx.android.synthetic.main.user_fragment.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.longToast

class UserFragment : Fragment(), RequestCallback {

    var nome: String = ""
    var email: String = ""
    var telefone: String = ""
    var idAvatar: String = ""
    var profileDialog = ProfileDialog(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_fragment, container, false)

        view.atualizar.setOnClickListener {
            getCampos()
            FirebaseMethods.atualizarUser(User(nome,email,telefone,idAvatar),this)
        }

        view.logout.setOnClickListener{
            FirebaseMethods.signOut(act)
        }

        view.profileImage.setOnClickListener {
            profileDialog.show(fragmentManager!!,"")
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        setCampos()
    }

    override fun onSuccess(objects: Any) {
        if(objects is Int){
            profileDialog.dismiss()
            idAvatar = objects.toString()
            profileImage.setImageResource(objects)
        }else{
            alert("Dados atualizados com sucesso!") {
                okButton { (act as HomeActivity).changeToFirstFragment() }
            }.apply {
                isCancelable = false
            }.show()
        }
    }

    override fun onError(msgError: String) {
        longToast(msgError)
    }

    private fun setCampos(){
        edit_perfil_email.setText(userLogged?.email)
        edit_perfil_nome.setText(userLogged?.nome)
        edit_perfil_telefone.setText(userLogged?.telefone)
        if(!userLogged?.idAvatar!!.isEmpty()){
            profileImage.setImageResource(userLogged?.idAvatar!!.toInt())
        }
    }

    private fun getCampos() {
        nome = edit_perfil_nome.text.toString()
        email = edit_perfil_email.text.toString()
        telefone = edit_perfil_telefone.text.toString()
    }
}