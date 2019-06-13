package br.ufpe.cin.petetive.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import br.ufpe.cin.petetive.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "FirebaseEmailPassword"

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        btn_email_sign_in.setOnClickListener(this)
        btn_email_create_account.setOnClickListener(this)
        btn_sign_out.setOnClickListener(this)
        btn_verify_email.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()

        //window.statusBarColor = Color.TRANSPARENT
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_email_create_account -> createAccount(edtEmail.text.toString(), edtPassword.text.toString())
            R.id.btn_email_sign_in -> signIn(edtEmail.text.toString(), edtPassword.text.toString())
            R.id.btn_sign_out -> signOut()
        }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val it = Intent(this, MainActivity::class.java)
                    startActivity(it)
                    finish()
                    // update UI with the signed-in user's information

                }
            }
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val it = Intent(this, MainActivity::class.java)
                    startActivity(it)
                    finish()
                    // update UI with the signed-in user's information
                    //val user = mAuth!!.getCurrentUser()
                    //updateUI(user)
                }
            }
    }

    private fun signOut() {
        mAuth!!.signOut()

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