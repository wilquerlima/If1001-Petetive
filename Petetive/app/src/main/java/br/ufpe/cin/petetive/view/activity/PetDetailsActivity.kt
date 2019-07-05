package br.ufpe.cin.petetive.view.activity

import android.os.Bundle
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.data.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pet_details_fragment.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

class PetDetailsActivity : AppCompatActivity() {

    var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pet_details_fragment)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.detalhes)

        pet = intent.getSerializableExtra("pet") as Pet?

        if (pet != null) {
            colocarInformacoes()
        } else {
            alert("Ops, ocorreu um erro, tente novamente!") {
                okButton { finish() }
            }.apply {
                isCancelable = false
            }.show()
        }
    }

    private fun colocarInformacoes() {
        if (!pet?.urlImage.isNullOrEmpty()) {
            Picasso.get().load(pet?.urlImage).into(imgPet)
        } else {
            Picasso.get().load(R.mipmap.placeholder).into(imgPet)
        }
        if (pet?.nome.isNullOrEmpty()) {
            nome.visibility = GONE
            appCompatTextView2.visibility = GONE
        } else {
            nome.text = pet?.nome
        }
        local.text = pet?.local
        raca.text = pet?.raca
        descricao.text = pet?.descricao
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}