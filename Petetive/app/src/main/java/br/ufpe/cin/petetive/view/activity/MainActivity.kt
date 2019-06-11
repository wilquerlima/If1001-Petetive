package br.ufpe.cin.petetive.view.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.view.fragment.CadastrarFragment
import br.ufpe.cin.petetive.view.fragment.MapFragment
import br.ufpe.cin.petetive.view.fragment.ProcurarFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var mBottomNav: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBottomNav = findViewById(R.id.bottom_navigation)
        mBottomNav?.setOnNavigationItemSelectedListener(this)
        mBottomNav?.selectedItemId = R.id.menu_item_procurar
        //val firstFragment = ProcurarFragment()
        //supportFragmentManager.beginTransaction().add(R.id.framelayout, firstFragment).commit()
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
                    replace(R.id.framelayout, CadastrarFragment())
                }.commit()
            }
            R.id.menu_item_map -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.framelayout, MapFragment())
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
