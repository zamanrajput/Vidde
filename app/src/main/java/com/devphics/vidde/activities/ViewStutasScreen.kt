package com.devphics.vidde.activities

import android.annotation.SuppressLint
import android.content.ContentValues

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.devphics.vidde.databinding.ActivityFullViewStatusBinding
import java.io.File
import java.io.FileOutputStream


class ViewStutasScreen : AppCompatActivity() {
    private lateinit var binding: ActivityFullViewStatusBinding
    var bundle: Bundle? = null
    private var MyFileList: String? = null
    var WhatsapFolderLocation: String? = null
    private val MyImageDownloadedPath = "/Status Saver/My Status Images"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullViewStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)




        MyFileList =intent.getStringExtra("MyFilesList").toString()
        WhatsapFolderLocation =intent.getStringExtra("FolderLocation").toString()
        binding.apply {
            Glide.with(this@ViewStutasScreen).load(MyFileList).into(statusImageView)

            btnDownload.setOnClickListener {

                if (DownloadingImage(statusImageView)) {
                    binding.btnDownload.visibility = View.VISIBLE
                    Toast.makeText(
                        this@ViewStutasScreen,
                        "Download Completed",
                        Toast.LENGTH_SHORT
                    ).show()
                    this@ViewStutasScreen.finish()
                } else {
                    binding.btnDownload.visibility = View.VISIBLE
                    Toast.makeText(
                        this@ViewStutasScreen,
                        "Download Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults.size > 0) {
                val write_str = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read_str = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write_str && read_str) {
                    Toast.makeText(
                        this,
                        "Permission Granted",
                        Toast.LENGTH_LONG
                    ).show()
                    //  DownloadingImage();
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun DownloadingImage(ViewingLargeImageview:ImageView): Boolean {
        binding.btnDownload.visibility = View.INVISIBLE
        val drawable = ViewingLargeImageview.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val sdCardDirectory = File(Environment.getExternalStorageDirectory(), MyImageDownloadedPath)
        if (!sdCardDirectory.exists()) {
            sdCardDirectory.mkdirs()
        }
        val MyImage = File(sdCardDirectory, "/Image" + System.currentTimeMillis() + ".jpg")
        var success = false
        val outputStream: FileOutputStream
        try {
            outputStream = FileOutputStream(MyImage)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.DATA, MyImage.absolutePath)
            this. contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            success = true
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
        }
        return success
    }



}

