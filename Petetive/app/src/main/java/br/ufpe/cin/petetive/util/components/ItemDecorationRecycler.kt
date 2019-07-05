package br.ufpe.cin.petetive.util.components

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class ItemDecorationRecycler(val height: Int) : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect){
            if (parent.getChildAdapterPosition(view) == 0){
                top = height
            }
            left =  height
            right = height
            bottom = height
        }
    }

}