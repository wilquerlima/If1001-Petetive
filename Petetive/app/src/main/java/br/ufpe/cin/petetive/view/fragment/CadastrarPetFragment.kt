package br.ufpe.cin.petetive.view.fragment

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.MemoryPolicy
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import com.google.android.gms.tasks.OnSuccessListener
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CadastrarPetFragment : Fragment(), View.OnClickListener {

    val CHOOSE_CAMERA_CODE = 101
    val CHOOSE_GALLERY_CODE = 102
    lateinit var dialog: DialogInterface
    lateinit var currentPhotoPath: String
    lateinit var urlImagePet: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cadastrar_pet_fragment, container, false)

        view.btn_cadastrar_pet.setOnClickListener(this)
        view.default_foto.setOnClickListener(this)
        view.photo.setOnClickListener(this)


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


    fun checkPermissions(permission: Int) {

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

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        ctx,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CHOOSE_CAMERA_CODE)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun abrirCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(ctx.packageManager)?.also {
                startActivityForResult(takePictureIntent, CHOOSE_CAMERA_CODE)
            }
        }
        //dispatchTakePictureIntent()
    }

    fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_GALLERY_CODE)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CHOOSE_CAMERA_CODE -> {
                    dialog.dismiss()
                    /*Picasso.get()
                        .load(currentPhotoPath)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(photo)*/
                    val imageBitmap = data!!.extras!!.get("data") as Bitmap
                    photo.setImageBitmap(imageBitmap)
                    default_foto.visibility = View.GONE
                    /*val bitmaps = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    picture!!.setImageBitmap(bitmaps)

                    */

                    /*
                    val extras = data!!.extras
                    var bitmap: Bitmap? = null
                    if (extras != null) {
                        bitmap = extras.get("data") as Bitmap
                    }
                    val file :File
                    file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                        val answer: String =  current.format(formatter)
                        File.createTempFile(answer, null, ctx.cacheDir)
                    } else {
                        val date = Date()
                        val formatter = SimpleDateFormat("dd MMM yyyy HH:mma")
                        val answer: String = formatter.format(date)
                        File.createTempFile(answer, null, ctx.cacheDir)
                    }
                    val fileOutputStream: FileOutputStream
                    try {
                        fileOutputStream = FileOutputStream(file)
                        assert(bitmap != null)
                        bitmap!!.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)
                        fileOutputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    //exibe bitmap no img_artista
                    Picasso.get().invalidate(file.path)
                    Picasso.get()
                        .load(file)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(photo)
                    default_foto.visibility = View.GONE*/
                }
                CHOOSE_GALLERY_CODE -> {
                    dialog.dismiss()
                    val selectedImage = data!!.data
                    val ref = FirebaseMethods.storageRef.child("images")

                    //desse jeito funciona, mas eu acho que demorou um pouco
                    val uploadTask = ref.putFile(selectedImage).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {

                            val url = it
                            urlImagePet = url.toString()

                        }.addOnFailureListener {

                        }
                    }

                    //desse jeito é como ta na documentação, eu achei mais rapido
                    //val uploadTask = ref.putFile(selectedImage)
                    /*val url = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        return@Continuation FirebaseMethods.storageRef.child("images/$idPet/photo.png").downloadUrl
                    }).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val downloadUri = it.result
                        }
                    }*/


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
        FirebaseMethods.petRef.child(key!!).setValue(
            Pet(
                urlImagePet,
                editLocal.text.toString(),
                editNome.text.toString(),
                editDescricao.text.toString(),
                editRaca.text.toString(),
                usersId,
                null
            )
        )
    }
}