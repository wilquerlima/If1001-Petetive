package br.ufpe.cin.petetive.controller

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.data.Pet
import br.ufpe.cin.petetive.view.activity.PetDetailsActivity
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
        val constraintCard = view.constraintCard

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

    inline fun SpannableStringBuilder.withSpan(
        span: Any,
        action: SpannableStringBuilder.() -> Unit
    ): SpannableStringBuilder {
        val from = length
        action()
        setSpan(span, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_animal, p0, false))
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.loadImage(petList[p1].urlImage)

        val local = SpannableStringBuilder()
            .withSpan(StyleSpan(android.graphics.Typeface.BOLD)) { append(context.getString(R.string.txtLocal)) }
            .append(" " + petList[p1].local)
        val nome = SpannableStringBuilder()
            .withSpan(StyleSpan(android.graphics.Typeface.BOLD)) { append(context.getString(R.string.txtNome)) }
            .append(" " + petList[p1].nome)
        val raca = SpannableStringBuilder()
            .withSpan(StyleSpan(android.graphics.Typeface.BOLD)) { append(context.getString(R.string.txtRaca)) }
            .append(" " + petList[p1].raca)
        val descricao = SpannableStringBuilder()
            .withSpan(StyleSpan(android.graphics.Typeface.BOLD)) { append(context.getString(R.string.txtDescricao)) }
            .append(" " + petList[p1].descricao)

        p0.txtLocalidade.text = local
        if (!petList[p1].nome.isBlank()) {
            p0.txtNome.text = nome
        } else {
            p0.txtNome.visibility = View.GONE
        }
        p0.txtRaca.text = raca
        p0.txtDescricao.text = descricao

        if(p0.txtDescricao.text.length > 45){
            val subStringDesc = SpannableStringBuilder()
                .withSpan(StyleSpan(android.graphics.Typeface.BOLD)) {append(context.getString(R.string.txtDescricao))}
                .append( " ${petList[p1].descricao.substring(0,40)}...")
            p0.txtDescricao.text = subStringDesc
        }

        p0.constraintCard.setOnClickListener {
            val intent = Intent(context, PetDetailsActivity::class.java)
            intent.putExtra("pet",petList[p1])
            context.startActivity(intent)
        }
    }


}