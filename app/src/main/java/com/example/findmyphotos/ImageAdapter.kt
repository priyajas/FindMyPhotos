package com.example.findmyphotos

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmyphotos.databinding.ImageCardBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var imgList: List<String> = ArrayList()
    // var actionMenu: (String) -> Unit = {}

    fun setImageList(imgCount: List<String>) {
        this.imgList = imgCount
        notifyDataSetChanged()
    }


    inner class ImageViewHolder(val binding: ImageCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imgCategory.setOnClickListener {
                // actionMenu.invoke(categoryList[position].strCategory)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.apply {
          imgCategory.setImageBitmap(BitmapFactory.decodeFile(imgList[position]))
        }

    }

    override fun getItemCount(): Int {
        return imgList.size
    }


}