package br.ufpe.cin.petetive.view.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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
import br.ufpe.cin.petetive.controller.Session.userLogged
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, RequestCallback {

    var mBottomNav: BottomNavigationView? = null
    var uid: String = ""
    var user: User? = null
    lateinit var notificationBadge: View
    var dialog: ProgressDialog? = null

    var fragment1 = ProcurarFragment()
    var fragment2 = CadastrarPetFragment()
    var fragment3 = MapFragment()
    var fragment4 = UserFragment()

    var active : Fragment = fragment1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid = intent.getStringExtra("uid")
        FirebaseMethods.getUser(uid, this)
        //dialog = indeterminateProgressDialog(message = "Aguarde um momento...")
        //dialog?.setCanceledOnTouchOutside(false)
        //setProgress(true)
        /*supportFragmentManager.beginTransaction().add(R.id.framelayout, fragment4,"4").hide(fragment4).commit()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, fragment3,"3").hide(fragment3).commit()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, fragment2,"2").hide(fragment2).commit()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, fragment1,"1").commit()*/

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
                /*supportFragmentManager.beginTransaction().hide(active).show(fragment1).commit()
                active = fragment1*/
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, ProcurarFragment())
                }.commit()
            }
            R.id.menu_item_cadastrar -> {
                /*supportFragmentManager.beginTransaction().hide(active).show(fragment2).commit()
                active = fragment2*/
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, CadastrarPetFragment())
                }.commit()
            }
            R.id.menu_item_map -> {
                /*supportFragmentManager.beginTransaction().hide(active).show(fragment3).commit()
                active = fragment3*/
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, MapFragment())
                }.commit()
            }
            R.id.menu_item_user -> {
                /*supportFragmentManager.beginTransaction().hide(active).show(fragment4).commit()
                active = fragment4*/
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

            /*notificationBadge = LayoutInflater.from(this).inflate(R.layout.custom_perfil, menuView, false)
            itemView.addView(notificationBadge)*/
            mBottomNav!!.showBadge(R.id.menu_item_user)


        } else {
            mBottomNav!!.removeBadge(R.id.menu_item_user)
            if (this::notificationBadge.isInitialized) {
                //mBottomNav!!.removeBadge(3)
                /*notificationBadge.visibility = GONE
                itemView.removeView(notificationBadge)
                mBottomNav?.removeView(notificationBadge)*/
            }
        }
    }
}
