package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.*
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.view.*
import org.jetbrains.anko.support.v4.toast

class CadastrarPetFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cadastrar_pet_fragment, container, false)

        view.btn_cadastrar_pet.setOnClickListener(this)
        view.default_foto.setOnClickListener(this)


        return view
    }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_cadastrar_pet ->{
                setProgress(true)
                if (checkValues()) {
                    toast("passou")
                    setProgress(false)
                }else{
                    setProgress(false)
                }
            }
            R.id.default_foto ->{}
        }
    }

    private fun setProgress(active : Boolean){
        btn_cadastrar_pet.isEnabled = !active

        if(active){
            progress_pet.visibility = View.VISIBLE
            btn_cadastrar_pet.text = ""
        }else{
            progress_pet.visibility = View.GONE
            btn_cadastrar_pet.text = getString(R.string.cadastrar_txt)
        }
    }

    private fun checkValues(): Boolean {
        return when {
            editLocal.text.toString().trim().isNullOrEmpty() -> {
                editLocal.error = "É necessário informar um local."
                false
            }
            editDescricao.text.toString().trim().isNullOrEmpty() -> {
                editDescricao.error = "É necessário informar uma desrição."
                false
            }
            editRaca.text.toString().trim().isNullOrEmpty() -> {
                editRaca.error = "É necessário informar uma raça."
                false
            }
            else -> true
        }

    }
}