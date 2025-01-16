package com.example.imagefromfolder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImagesAdapter(private val imageData: ArrayList<String>): RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {
    class ImagesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_row, parent, false))
    }

    override fun getItemCount(): Int {
        return imageData.size
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val bitmapImageUrl: Bitmap = BitmapFactory.decodeFile(imageData[position])
        holder.imageView.setImageBitmap(bitmapImageUrl)
    }
}