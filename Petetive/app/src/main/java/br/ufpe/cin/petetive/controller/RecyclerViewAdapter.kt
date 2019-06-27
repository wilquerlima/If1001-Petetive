package br.ufpe.cin.petetive.controller

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.data.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_animal.view.*

class RecyclerViewAdapter(
    var petList: MutableList<Pet>,
    private val context: Context
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPet = view.imgPet
        val txtLocalidade = view.txtLocalidade
        val txtNome = view.txtNome
        val txtDescricao = view.txtDescricao
        val txtRaca = view.txtRaca

        fun loadImage(urlImage: String) {
            if (urlImage.isNullOrBlank()) {
                Picasso.get()
                    .load(R.mipmap.placeholder)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .fit()
                    .into(imgPet)
            } else {
                Picasso.get()
                    .load(urlImage)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .fit()
                    .into(imgPet)
            }

        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_animal, p0, false))
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.loadImage(petList[p1].urlImage)

        p0.txtLocalidade.text = String.format(context.getString(R.string.txtLocal), petList[p1].local)
        if (!petList[p1].nome.isNullOrBlank()) {
            p0.txtNome.text = String.format(context.getString(R.string.txtNome), petList[p1].nome)
        } else {
            p0.txtNome.visibility = View.GONE
        }
        p0.txtDescricao.text = String.format(context.getString(R.string.txtDescricao), petList[p1].descricao)
        p0.txtRaca.text = String.format(context.getString(R.string.txtRaca), petList[p1].raca)
    }


}