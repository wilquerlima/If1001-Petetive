package br.ufpe.cin.petetive.view.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.data.User
import br.ufpe.cin.petetive.view.fragment.CadastrarPetFragment
import br.ufpe.cin.petetive.view.fragment.MapFragment
import br.ufpe.cin.petetive.view.fragment.ProcurarFragment
import br.ufpe.cin.petetive.view.fragment.UserFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var mBottomNav: BottomNavigationView? = null
    var uid : String = ""
    var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uid = intent.getStringExtra("uid")
        FirebaseMethods.userRef.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)
            }

        })

        mBottomNav = findViewById(R.id.bottom_navigation)
        mBottomNav?.setOnNavigationItemSelectedListener(this)
        mBottomNav?.selectedItemId = R.id.menu_item_procurar
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
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
}
