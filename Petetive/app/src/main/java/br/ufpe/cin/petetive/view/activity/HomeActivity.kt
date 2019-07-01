package br.ufpe.cin.petetive.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.controller.RequestCallback
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.fragment.CadastrarPetFragment
import br.ufpe.cin.petetive.view.fragment.MapFragment
import br.ufpe.cin.petetive.view.fragment.ProcurarFragment
import br.ufpe.cin.petetive.view.fragment.UserFragment
import org.jetbrains.anko.longToast
import android.view.View.GONE
import android.view.View.VISIBLE
import br.ufpe.cin.petetive.controller.Session.userLogged
import org.jetbrains.anko.indeterminateProgressDialog


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, RequestCallback {

    var mBottomNav: BottomNavigationView? = null
    var uid: String = ""
    var user: User? = null
    lateinit var notificationBadge: View
    var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid = intent.getStringExtra("uid")
        //dialog = indeterminateProgressDialog(message = "Aguarde um momento...")
        //dialog?.setCanceledOnTouchOutside(false)
        //setProgress(true)
        FirebaseMethods.getUser(uid, this)
        mBottomNav = findViewById(R.id.bottom_navigation)
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
        val menuView = mBottomNav?.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(3) as BottomNavigationItemView

        if (user?.email.isNullOrBlank() || user?.telefone.isNullOrBlank() || user?.nome.isNullOrEmpty()) {

            notificationBadge = LayoutInflater.from(this).inflate(R.layout.custom_perfil, menuView, false)
            itemView.addView(notificationBadge)


        } else {
            if (this::notificationBadge.isInitialized) {
                notificationBadge.visibility = GONE
                itemView.removeView(notificationBadge)
                mBottomNav?.removeView(notificationBadge)
            }
        }
    }
}
