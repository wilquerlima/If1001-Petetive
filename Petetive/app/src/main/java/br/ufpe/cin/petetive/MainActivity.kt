package br.ufpe.cin.petetive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    var mBottomNav: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBottomNav = findViewById(R.id.bottom_navigation)
        mBottomNav?.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.menu_item_procurar -> toast("Procurar clicado").duration = Toast.LENGTH_LONG
            R.id.menu_item_cadastrar -> toast("Cadastrar clicado").duration = Toast.LENGTH_LONG
            R.id.menu_item_map -> toast("Mapa Clicado").duration = Toast.LENGTH_LONG
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_menu, menu)
        return true
    }
}
