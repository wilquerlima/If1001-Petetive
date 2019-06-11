package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import kotlinx.android.synthetic.main.procurar_fragment.view.*

class ProcurarFragment: Fragment() {

    var rv: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.procurar_fragment,container,false)
        rv = view.recycle_procurar
        return view
    }
}