package com.devphics.vidde.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devphics.vidde.databinding.CurrentImageStatusAvailableRowLayoutBinding
import com.devphics.vidde.activities.ViewStutasScreen
import java.io.File
import java.util.ArrayList

class WhatsAppImagesAdapter(
    private var modelList: ArrayList<File>?,
    private var context: Context,
    private var whatsAppImageLocation:String?
) :
    RecyclerView.Adapter<WhatsAppImagesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CurrentImageStatusAvailableRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(modelList!![position])
    }

    override fun getItemCount() = modelList!!.size

    inner class ViewHolder(val binding:  CurrentImageStatusAvailableRowLayoutBinding):
        RecyclerView.ViewHolder(binding.root){
        fun onBind(data:File){
            binding.apply{
                Glide.with(context).load(data).into(statusImageView)

                ImageCardView.setOnClickListener {
                    val intent = Intent(context, ViewStutasScreen::class.java)
                    intent.putExtra("FolderLocation", whatsAppImageLocation)
                    intent.putExtra("MyActivity", "MyAvailableStatus")
                    intent.putExtra("MyFilesList", data.toString())
                    context.startActivity(intent)

                }

            }
        }
    }


}