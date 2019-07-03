package br.ufpe.cin.petetive.util.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import br.ufpe.cin.petetive.R
import br.ufpe.cin.petetive.util.controllers.RequestCallback
import br.ufpe.cin.petetive.util.adapters.RecyclerImagesAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.profile_dialog.view.*
import org.jetbrains.anko.support.v4.ctx

class ProfileDialog(var requestCallback: RequestCallback) : DialogFragment(){

    var dialogProfile : Dialog? = null
    var recycleProfile : RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_dialog,container,false)

        recycleProfile = view.recycle_profile

        val layoutManager = FlexboxLayoutManager(ctx)
        layoutManager.flexDirection = FlexDirection.ROW
        recycleProfile!!.layoutManager = layoutManager
        recycleProfile!!.adapter = RecyclerImagesAdapter(ctx, requestCallback)

        view.cancelar.setOnClickListener {
            dialog?.dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogProfile = super.onCreateDialog(savedInstanceState)
        dialogProfile?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false
        dialogProfile?.setCanceledOnTouchOutside(false)

        return dialogProfile!!
    }
}