package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.controller.RequestCallback
import br.ufpe.cin.petetive.controller.Session
import br.ufpe.cin.petetive.controller.Session.userLogged
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.activity.HomeActivity
import kotlinx.android.synthetic.main.user_fragment.*
import kotlinx.android.synthetic.main.user_fragment.view.*
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.longToast

class UserFragment : Fragment(), RequestCallback {

    var nome: String = ""
    var email: String = ""
    var telefone: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_fragment, container, false)

        view.btn_atualizar_perfil.setOnClickListener {
            getCampos()
            FirebaseMethods.atualizarUser(User(nome,email,telefone),this)
        }

        view.logout.setOnClickListener{
            FirebaseMethods.signOut(act)
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        setCampos()
    }

    override fun onSuccess(objects: Any) {
        alert("Dados atualizados com sucesso!") {
            okButton { (act as HomeActivity).changeToFirstFragment() }
        }.apply {
            isCancelable = false
        }.show()
    }

    override fun onError(msgError: String) {
        longToast(msgError)
    }

    private fun setCampos(){
        edit_perfil_email.setText(userLogged?.email)
        edit_perfil_nome.setText(userLogged?.nome)
        edit_perfil_telefone.setText(userLogged?.telefone)
    }

    private fun getCampos() {
        nome = edit_perfil_nome.text.toString()
        email = edit_perfil_email.text.toString()
        telefone = edit_perfil_telefone.text.toString()
    }
}