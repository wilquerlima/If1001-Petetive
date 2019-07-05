package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.util.controllers.FirebaseMethods
import br.ufpe.cin.petetive.util.components.ItemDecorationRecycler
import br.ufpe.cin.petetive.util.adapters.RecyclerViewAdapter
import br.ufpe.cin.petetive.util.controllers.RequestCallback
import br.ufpe.cin.petetive.data.Pet
import kotlinx.android.synthetic.main.perdidos_fragment.*
import kotlinx.android.synthetic.main.perdidos_fragment.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.longToast

class PerdidosFragment : Fragment(), RequestCallback {

    var rv: RecyclerView? = null
    var petListPerdido : MutableList<Pet> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.perdidos_fragment, container, false)

        rv = view.recycle_perdidos
        rv!!.apply {
            layoutManager = LinearLayoutManager(ctx)
            adapter = RecyclerViewAdapter(petListPerdido, ctx)
            addItemDecoration(ItemDecorationRecycler(20))
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        petListPerdido.clear()
        FirebaseMethods.getPets(this,true)
        progressPerdidos?.visibility = View.VISIBLE
    }

    override fun onSuccess(objects: Any) {
        if(objects is MutableList<*>){
            petListPerdido.addAll(objects.filterIsInstance<Pet>().toMutableList())
        }

        if(petListPerdido.isNullOrEmpty()){
            longToast("Sem pets cadastrados")
        } else {
            rv!!.adapter!!.notifyDataSetChanged()
        }
        progressPerdidos?.visibility = View.GONE
    }

    override fun onError(msgError: String) {
        progressPerdidos?.visibility = View.GONE
        //longToast(msgError)
    }
}