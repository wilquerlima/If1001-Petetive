package br.ufpe.cin.petetive.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.util.controllers.FirebaseMethods
import br.ufpe.cin.petetive.util.controllers.RequestCallback
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_activity.*
import org.jetbrains.anko.longToast


class LoginActivity : AppCompatActivity(), View.OnClickListener, RequestCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        btn_email_sign_in.setOnClickListener(this)
        btn_email_create_account.setOnClickListener(this)
        btn_sign_out.setOnClickListener(this)
        btn_verify_email.setOnClickListener(this)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseMethods.mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val it = Intent(this, HomeActivity::class.java)
            it.putExtra("uid", currentUser.uid)
            startActivity(it)
            setProgress(false, 2)
            finish()
        }
    }

    private fun setProgress(active: Boolean, button: Int) {
        btn_email_sign_in.isEnabled = !active
        btn_email_create_account.isEnabled = !active

        when (button) {
            1 -> {
                if (active) {
                    progressLogin.visibility = View.VISIBLE
                    btn_email_sign_in.text = ""
                } else {
                    progressLogin.visibility = View.GONE
                    btn_email_sign_in.text = getString(R.string.login)
                }
            }
            2 -> {
                if (active) {
                    progressCadastrar.visibility = View.VISIBLE
                    btn_email_create_account.text = ""
                } else {
                    progressCadastrar.visibility = View.GONE
                    btn_email_create_account.text = getString(R.string.cadastrar_txt)
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
        }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }
        setProgress(true, 2)
        FirebaseMethods.createAccount(email, password, this, this)
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm(email, password)) {
            return
        }
        setProgress(true, 1)
        FirebaseMethods.signIn(email, password, this, this)
    }

    override fun onSuccess(objects: Any) {
        if (objects is List<*>) {
            val it = Intent(this, HomeActivity::class.java)
            it.putExtra("uid", objects[0].toString())
            startActivity(it)
            setProgress(false, objects[1].toString().toInt())
            finish()
        }
    }

    override fun onError(msgError: String) {
        setProgress(false, 1)
        setProgress(false, 2)
        longToast(msgError)
    }

    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            longToast("Enter email address!")
            return false
        }

        if (TextUtils.isEmpty(password)) {
            longToast("Enter password!")
            return false
        }

        if (password.length < 6) {
            longToast("Password too short, enter minimum 6 characters!")
            return false
        }

        return true
    }


}