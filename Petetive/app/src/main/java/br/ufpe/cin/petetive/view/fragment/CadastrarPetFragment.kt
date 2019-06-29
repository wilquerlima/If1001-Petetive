package br.ufpe.cin.petetive.view.fragment

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.drm.DrmStore
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import android.view.ViewGroup
import android.widget.LinearLayout
import br.ufpe.cin.petetive.R
import com.squareup.picasso.Picasso
import br.ufpe.cin.petetive.data.Pet
import br.ufpe.cin.petetive.controller.FirebaseMethods
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.*
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast

class CadastrarPetFragment : Fragment(), View.OnClickListener {

    val CHOOSE_CAMERA_CODE = 101
    val CHOOSE_GALLERY_CODE = 102
    lateinit var dialog : DialogInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cadastrar_pet_fragment, container, false)

        view.btn_cadastrar_pet.setOnClickListener(this)
        view.default_foto.setOnClickListener(this)


        return view
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_cadastrar_pet -> {
                setProgress(true)


                if (checkValues()) {
                    toast("Seu Pet foi Cadastrado com sucesso")
                    cadastrar()

                    setProgress(false)
                } else {
                    setProgress(false)
                }
            }
            R.id.photo -> {
                showAlert()
            }
            R.id.default_foto -> {
                showAlert()
            }
        }
    }

    private fun setProgress(active: Boolean) {
        btn_cadastrar_pet.isEnabled = !active

        if (active) {
            progress_pet.visibility = View.VISIBLE
            btn_cadastrar_pet.text = ""
        } else {
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
                editDescricao.error = "É necessário informar uma descrição."
                false
            }
            editRaca.text.toString().trim().isNullOrEmpty() -> {
                editRaca.error = "É necessário informar uma raça."
                false
            }
            else -> true
        }

    }


    fun checkPermissions(permission : Int){
        when (permission) {
            1 -> {

            }
            2 -> if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }else{
                abrirGaleria()
            }
        }
    }

    fun abrirGaleria(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            when(requestCode){
                CHOOSE_CAMERA_CODE -> {

                }
                CHOOSE_GALLERY_CODE -> {
                    dialog.dismiss()
                    val selectedImage = data!!.data
                    val path = selectedImage!!.path
                    val idPet = FirebaseMethods.petRef.push().key

                    var file = Uri.fromFile(File(path))
                    val uploadTask = FirebaseMethods.storageRef.child("images/${idPet}/photo").putFile(selectedImage)
                    


                    Picasso.get()
                        .load(selectedImage)
                        .error(R.mipmap.placeholder)
                        .into(photo)
                    default_foto.visibility = View.GONE
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1->{
                if (grantResults.isNotEmpty() && grantResults.get(0) ==PackageManager.PERMISSION_GRANTED){
                    abrirGaleria()
                }
                else {
                    toast( "You denied the permission")
                }
            }
        }
    }

    private fun showAlert() {
        dialog = alert("Escolha uma opção para adicionar a foto:") {
            customView {
                verticalLayout {
                    button("Câmera") {
                        textColor = ContextCompat.getColor(ctx, R.color.corSecundaria)
                        background = ContextCompat.getDrawable(ctx, R.drawable.button_style_login)
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                        marginEnd = dip(10)
                        marginStart = dip(10)
                        topMargin = dip(10)
                    }.setOnClickListener {

                    }
                    button("Galeria") {
                        textColor = ContextCompat.getColor(ctx, R.color.corSecundaria)
                        background = ContextCompat.getDrawable(ctx, R.drawable.button_style_cadastrar)
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                        marginEnd = dip(10)
                        marginStart = dip(10)
                        topMargin = dip(10)
                    }.setOnClickListener {
                        checkPermissions(2)
                    }
                }
            }
            cancelButton {

            }
        }.show()
    }
    private fun cadastrar() {
        val key = FirebaseMethods.petRef.push().key

        val usersId = FirebaseMethods.mAuth.currentUser?.uid.toString()
        FirebaseMethods.petRef.child(key!!).setValue(Pet("",editLocal.text.toString(), editNome.text.toString(), editDescricao.text.toString(), editRaca.text.toString(), usersId, null))
    }
}