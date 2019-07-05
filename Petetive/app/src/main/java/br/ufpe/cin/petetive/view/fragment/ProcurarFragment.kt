package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.util.adapters.PagerAdapter
import kotlinx.android.synthetic.main.procurar_fragment.*
import kotlinx.android.synthetic.main.procurar_fragment.view.*

class ProcurarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.procurar_fragment, container, false)

        val fragmentAdapter = PagerAdapter(activity!!.supportFragmentManager)
        fragmentAdapter.addFragment(PerdidosFragment(), getString(R.string.perdidos))
        fragmentAdapter.addFragment(EncontradosFragment(), getString(R.string.encontrados))
        view.viewPager.adapter = fragmentAdapter
        view.tabLayout.setupWithViewPager(viewPager)

        return view
    }
}