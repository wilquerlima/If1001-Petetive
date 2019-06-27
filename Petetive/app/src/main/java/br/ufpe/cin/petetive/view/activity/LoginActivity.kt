package br.ufpe.cin.petetive.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*
import org.jetbrains.anko.longToast


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FirebaseEmailPassword"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        btn_email_sign_in.setOnClickListener(this)
        btn_email_create_account.setOnClickListener(this)
        btn_sign_out.setOnClickListener(this)
        btn_verify_email.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()


        //window.statusBarColor = Color.TRANSPARENT
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?){
        if(currentUser != null){
            val it = Intent(this, HomeActivity::class.java)
            it.putExtra("uid", currentUser.uid)
            startActivity(it)
            setProgress(false,2)
            finish()
        }
    }

    fun setProgress(active: Boolean, button: Int) {
        btn_email_sign_in.isEnabled = !active
        btn_email_create_account.isEnabled = !active

        when (button) {
            1 -> {
                if(active){
                    progressLogin.visibility = View.VISIBLE
                    btn_email_sign_in.text = ""
                }else{
                    progressLogin.visibility = View.GONE
                    btn_email_sign_in.text = "Entrar"
                }
            }
            2 -> {
                if(active){
                    progressCadastrar.visibility = View.VISIBLE
                    btn_email_create_account.text = ""
                }else{
                    progressCadastrar.visibility = View.GONE
                    btn_email_create_account.text = "Cadastrar"
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_email_create_account -> {
                createAccount(edtEmail.text.toString(), edtPassword.text.toString())
            }
            R.id.btn_email_sign_in -> {
                signIn(edtEmail.text.toString(), edtPassword.text.toString())
            }
            R.id.btn_sign_out -> signOut()
        }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }
        setProgress(true,2)

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = mAuth.currentUser
                    val user = User("", currentUser?.email!!)
                    FirebaseMethods.userRef.child(currentUser.uid).setValue(user)

                    val it = Intent(this, HomeActivity::class.java)
                    it.putExtra("uid", task.result?.user?.uid)
                    startActivity(it)
                    setProgress(false,2)
                    finish()
                    // update UI with the signed-in user's information

                }else{
                    setProgress(false,2)
                    longToast("Erro Cadastrar")
                }
            }
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }

        setProgress(true,1)
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = mAuth.currentUser
                    val user = User("", currentUser?.email!!)
                    FirebaseMethods.userRef.child(currentUser.uid).setValue(user)

                    val it = Intent(this, HomeActivity::class.java)
                    it.putExtra("uid", currentUser.uid)
                    startActivity(it)
                    setProgress(false,1)
                    finish()
                }else{
                    setProgress(false,1)
                    longToast("Erro login")
                }
            }
    }

    private fun signOut() {
        mAuth.signOut()

    }


    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }


}