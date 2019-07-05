package br.ufpe.cin.petetive.view.fragment

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.util.controllers.FirebaseMethods
import br.ufpe.cin.petetive.util.controllers.RequestCallback
import br.ufpe.cin.petetive.view.activity.HomeActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.*
import kotlinx.android.synthetic.main.cadastrar_pet_fragment.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.io.ByteArrayOutputStream


class CadastrarPetFragment : Fragment(), View.OnClickListener, RequestCallback, RadioGroup.OnCheckedChangeListener {

    private val CHOOSE_CAMERA_CODE = 101
    private val CHOOSE_GALLERY_CODE = 102
    lateinit var dialog: DialogInterface
    var selectedImage: Uri? = null
    private var perdido: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cadastrar_pet_fragment, container, false)

        view.btn_cadastrar_pet.setOnClickListener(this)
        view.default_foto.setOnClickListener(this)
        view.photo.setOnClickListener(this)

        view.radioGroup.setOnCheckedChangeListener(this)

        return view
    }

    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        perdido = p1 == R.id.perdido
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_cadastrar_pet -> {
                setProgress(true)
                if (checkValues()) {
                    cadastrar()
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
            editLocal.text.toString().trim().isEmpty() -> {
                editLocal.error = "É necessário informar um local."
                false
            }
            editDescricao.text.toString().trim().isEmpty() -> {
                editDescricao.error = "É necessário informar uma descrição."
                false
            }
            editRaca.text.toString().trim().isEmpty() -> {
                editRaca.error = "É necessário informar uma raça."
                false
            }
            selectedImage == null -> {
                toast("É necessário informar uma foto.")
                false
            }
            radioGroup.checkedRadioButtonId == -1 -> {
                toast("É necessário escolher uma das opções de pet.")
                false
            }
            else -> true
        }

    }

    private fun checkPermissions(permission: Int) {

        when (permission) {
            1 -> {
                if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 2)
                } else {
                    abrirCamera()
                }
            }
            2 -> {
                if (ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                } else {
                    abrirGaleria()
                }
            }
        }
    }

    private fun abrirCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                startActivityForResult(takePictureIntent, CHOOSE_CAMERA_CODE)
            }
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_GALLERY_CODE)
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
        val path = MediaStore.Images.Media.insertImage(ctx.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getFullImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(ctx.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = ((width / bitmapRatio).toInt())
        } else {
            height = maxSize
            width = ((height * bitmapRatio).toInt())
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CHOOSE_CAMERA_CODE -> {
                    dialog.dismiss()

                    val imageBitmap = data!!.extras!!.get("data") as Bitmap
                    photo.setImageBitmap(imageBitmap)
                    selectedImage = getImageUri(imageBitmap)
                    default_foto.visibility = View.GONE
                    Picasso.get()
                        .load(selectedImage)
                        .error(R.mipmap.placeholder)
                        .into(photo)
                }
                CHOOSE_GALLERY_CODE -> {
                    dialog.dismiss()
                    selectedImage = data!!.data
                    //val bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver, selectedImage)
                    default_foto.visibility = View.GONE

                    val imageStream = ctx.contentResolver.openInputStream(selectedImage!!)
                    var selectedImagee = BitmapFactory.decodeStream(imageStream)

                    selectedImagee = getResizedBitmap(selectedImagee, 400)
                    selectedImage = getFullImageUri(selectedImagee)

                    Picasso.get()
                        .load(selectedImage)
                        .error(R.mipmap.placeholder)
                        .into(photo)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirGaleria()
                } else {
                    toast("You denied the permission")
                }
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirCamera()
                } else {
                    toast("You denied the permission")
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
                        checkPermissions(1)
                    }
                    button("Galeria") {
                        textColor = ContextCompat.getColor(ctx, R.color.corSecundaria)
                        background = ContextCompat.getDrawable(ctx, R.drawable.button_style_cadastrar)
                    }.lparams {
                        width = LinearLayout.LayoutParams.MATCH_PARENT
                        marginEnd = dip(10)
                        marginStart = dip(10)
                        topMargin = dip(10)
                        bottomMargin = dip(10)
                    }.setOnClickListener {
                        checkPermissions(2)
                    }
                }
            }
        }.show()
    }

    private fun cadastrar() {
        FirebaseMethods.cadastrarPet(
            editLocal.text.toString(),
            editNome.text.toString(),
            editDescricao.text.toString(),
            editRaca.text.toString(),
            perdido,
            selectedImage!!,
            this
        )
    }

    override fun onSuccess(objects: Any) {
        setProgress(false)
        alert(objects.toString()) {
            okButton { (act as HomeActivity).changeToFirstFragment() }
        }.apply {
            isCancelable = false
        }.show()
    }

    override fun onError(msgError: String) {
        setProgress(false)
        toast(msgError)
    }
}