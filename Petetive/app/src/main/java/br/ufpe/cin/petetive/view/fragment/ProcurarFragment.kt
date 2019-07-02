package br.ufpe.cin.petetive.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.controller.FirebaseMethods
import br.ufpe.cin.petetive.controller.ItemDecorationRecycler
import br.ufpe.cin.petetive.controller.RecyclerViewAdapter
import br.ufpe.cin.petetive.controller.RequestCallback
import br.ufpe.cin.petetive.data.Pet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.procurar_fragment.*
import kotlinx.android.synthetic.main.procurar_fragment.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.longToast

class ProcurarFragment : Fragment(), RequestCallback {

    var rv: RecyclerView? = null
    var petList : MutableList<Pet> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.procurar_fragment, container, false)
        rv = view.recycle_procurar

        rv!!.apply {
            layoutManager = LinearLayoutManager(ctx)
            adapter = RecyclerViewAdapter(petList, ctx)
            addItemDecoration(ItemDecorationRecycler(20))
        }

       //val key = FirebaseMethods.petRef.push().key

        //FirebaseMethods.petRef.child(key!!).setValue(Pet("","Teste local", "Paulo", "Foi achado em Ouro Preto", "Fila Brasileiro", "3LJ5AO6H1JPlNi6cFZjYntldehY2", null))

        /*FirebaseMethods.petRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(pet in p0.children){
                    petList.add(pet.getValue(Pet::class.java)!!)
                }
                rv!!.adapter!!.notifyDataSetChanged()
            }

        })*/

        return view
    }

    override fun onResume() {
        super.onResume()
        petList.clear()
        FirebaseMethods.getPets(this)
        progress?.visibility = View.VISIBLE
    }

    override fun onSuccess(objects: Any) {
        if(objects is MutableList<*>){
            petList.addAll(objects.filterIsInstance<Pet>().toMutableList())
        }

        if(petList.isNullOrEmpty()){
            longToast("Sem pets cadastrados")
        } else {
            rv!!.adapter!!.notifyDataSetChanged()
        }
        progress?.visibility = View.GONE
    }

    override fun onError(msgError: String) {
        progress?.visibility = View.GONE
        longToast(msgError)
    }
}