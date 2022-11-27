package com.devphics.vidde.util

import android.R
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.provider.ContactsContract
import java.io.File


open class Utilities {
    companion object{
        fun alertDialouge(title:String,message:String,context:Context){
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message) // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(
                    "Okay",
                    DialogInterface.OnClickListener { dialog, which ->
                        // Continue with delete operation
                    }) // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(R.string.no, null)
                .setIcon(R.drawable.ic_dialog_alert)
                .show()
        }




        fun getFileName(fileName: String): String {
            val len = fileName.length
            var start = len - 1
            val temp = fileName.toCharArray()
            while (true) {
                if (temp[start] == '/') break
                start--
                if (start == -1) break
            }
            return fileName.substring(start + 1)
        }


        val STATUS_DIRECTORY_WHATSAPP = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator + "WhatsApp/Media/.Statuses"
        )

        val STATUS_DIRECTORY_NEW_WHATSAPP = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses")
        )


        val STATUS_DIRECTORY_WHATSAPP_BA = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator + "WhatsApp Business/Media/.Statuses"
        )

        val STATUS_DIRECTORY_NEW_WHATSAPP_BA = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses")
        )



        val STATUS_DIRECTORY_WHATSAPP_GB = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator + "GBWhatsApp/Media/.Statuses"
        )

        val STATUS_DIRECTORY_NEW_WHATSAPP_GB = File(
            (Environment.getExternalStorageDirectory().toString() +
                    File.separator + "Android/media/com.whatsapp/GBWhatsApp/Media/.Statuses")
        )

    }

}