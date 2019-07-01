package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import kotlinx.android.synthetic.main.user_fragment.*
import kotlinx.android.synthetic.main.user_fragment.view.*

class UserFragment : Fragment(){

    var nome : String = ""
    var email : String = ""
    var telefone : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.user_fragment,container,false)

        view.btn_atualizar_perfil.setOnClickListener{

        }

        return view
    }

    private fun setProgress(active : Boolean){
        btn_atualizar_perfil.isEnabled = !active

        if(active){
            progress_perfil.visibility = View.VISIBLE
            btn_atualizar_perfil.text = ""
        }else{
            progress_perfil.visibility = View.GONE
            btn_atualizar_perfil.text = getString(R.string.atualizar)
        }
    }

    private fun getCampos(){
        nome = edit_perfil_nome.text.toString()
        email = edit_perfil_email.text.toString()
        telefone = edit_perfil_telefone.text.toString()
    }
}