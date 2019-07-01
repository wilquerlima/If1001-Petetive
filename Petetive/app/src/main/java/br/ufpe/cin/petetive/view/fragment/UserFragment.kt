package br.ufpe.cin.petetive.view.fragment

import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.user_fragment.*
import kotlinx.android.synthetic.main.user_fragment.view.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.yesButton

class UserFragment : Fragment(), RequestCallback {

    var nome: String = ""
    var email: String = ""
    var telefone: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_fragment, container, false)

        view.btn_atualizar_perfil.setOnClickListener {
            setProgress(true)
            getCampos()
            FirebaseMethods.atualizarUser(User(nome,email,telefone),this)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        setCampos()
    }

    override fun onSuccess(objects: Any) {
        alert("Dados atualizados com sucesso!") {
            okButton { }
        }.show()
        setProgress(false)
    }

    override fun onError(msgError: String) {
        setProgress(false)
        longToast(msgError)
    }

    private fun setProgress(active: Boolean) {
        btn_atualizar_perfil.isEnabled = !active

        if (active) {
            progress_perfil?.visibility = View.VISIBLE
            btn_atualizar_perfil.text = ""
        } else {
            progress_perfil?.visibility = View.GONE
            btn_atualizar_perfil.text = getString(R.string.atualizar)
        }
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