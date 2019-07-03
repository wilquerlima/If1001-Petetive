package br.ufpe.cin.petetive.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.controller.RequestCallback
import br.ufpe.cin.petetive.controller.Session.userLogged
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.fragment.CadastrarPetFragment
import br.ufpe.cin.petetive.view.fragment.MapFragment
import br.ufpe.cin.petetive.view.fragment.ProcurarFragment
import br.ufpe.cin.petetive.view.fragment.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, RequestCallback {

    var mBottomNav: BottomNavigationView? = null
    var uid: String = ""
    var user: User? = null
    var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid = intent.getStringExtra("uid")
        FirebaseMethods.getUser(uid, this)

        mBottomNav = bottom_navigation
        mBottomNav?.setOnNavigationItemSelectedListener(this)
        mBottomNav?.selectedItemId = R.id.menu_item_procurar

    }

    private fun setProgress(active : Boolean){
        if (active) {
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }

    override fun onSuccess(objects: Any) {
        if (objects is User) {
            user = objects
            userLogged = objects
        }
        setUpBadge()
        //setProgress(false)
    }

    fun changeToFirstFragment(){
        mBottomNav?.selectedItemId = R.id.menu_item_procurar
    }

    override fun onError(msgError: String) {
        longToast(msgError)
        //setProgress(false)
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        if(user != null){
            setUpBadge()
        }
        when (p0.itemId) {
            R.id.menu_item_procurar -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, ProcurarFragment())
                }.commit()
            }
            R.id.menu_item_cadastrar -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, CadastrarPetFragment())
                }.commit()
            }
            R.id.menu_item_map -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, MapFragment())
                }.commit()
            }
            R.id.menu_item_user -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, UserFragment())
                }.commit()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (mBottomNav?.selectedItemId != R.id.menu_item_procurar) {
            mBottomNav?.selectedItemId = R.id.menu_item_procurar
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpBadge() {
        if (user?.email.isNullOrBlank() || user?.telefone.isNullOrBlank() || user?.nome.isNullOrEmpty()) {
            mBottomNav!!.showBadge(R.id.menu_item_user)
        } else {
            mBottomNav!!.removeBadge(R.id.menu_item_user)
        }
    }
}
