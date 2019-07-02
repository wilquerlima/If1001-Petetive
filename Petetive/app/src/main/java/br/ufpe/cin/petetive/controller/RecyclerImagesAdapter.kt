package br.ufpe.cin.petetive.controller

import android.content.Context
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.petetive.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image.view.*

class RecyclerImagesAdapter(var context: Context) : RecyclerView.Adapter<RecyclerImagesAdapter.ViewHolder>() {

    val listImages = listOf(
        R.mipmap.woman01, R.mipmap.woman02, R.mipmap.woman03, R.mipmap.woman04,
        R.mipmap.woman05, R.mipmap.woman06, R.mipmap.woman07, R.mipmap.woman08, R.mipmap.woman09, R.mipmap.woman10,
        R.mipmap.woman11, R.mipmap.woman12, R.mipmap.woman13, R.mipmap.woman14, R.mipmap.woman15, R.mipmap.man01,
        R.mipmap.man02, R.mipmap.man03, R.mipmap.man04, R.mipmap.man05, R.mipmap.man06, R.mipmap.man07,
        R.mipmap.man08, R.mipmap.man09, R.mipmap.man10, R.mipmap.man11, R.mipmap.man12, R.mipmap.man13,
        R.mipmap.man14, R.mipmap.man15
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: AppCompatImageView = view.images_profiles
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return RecyclerImagesAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, p0, false))
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Picasso.get()
            .load(listImages[p1])
            .placeholder(R.mipmap.user)
            .error(R.mipmap.user)
            .fit()
            .into(p0.image)
    }

}