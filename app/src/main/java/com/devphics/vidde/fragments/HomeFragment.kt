package com.devphics.vidde.fragments

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import com.devphics.vidde.`package`.FloatingWindow
import com.devphics.vidde.activities.FacebookActivity
import com.devphics.vidde.activities.TwitterActivity
import com.devphics.vidde.activities.WebActivity
import com.devphics.vidde.activities.WhatsAppActivity
import com.devphics.vidde.databinding.FragmenntHomescreenBinding
import com.devphics.vidde.twitter.TwitterVideoDownloader
import com.devphics.vidde.util.CommonClassForAPI
import com.google.android.gms.ads.*


class HomeFragment : Fragment() {

    var downloadId: Long? = null
    var commonClassForAPI: CommonClassForAPI? = null
     var dl_progress: Int = 0
    var bytes_downloaded: Int = 0
    private var pDialog: ProgressDialog? = null
    private lateinit var binding: FragmenntHomescreenBinding
    private var authorname: String? = null
    lateinit var mAdView: AdView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmenntHomescreenBinding.inflate(layoutInflater, container, false)



        mAdView = binding.adViewHomeBanner

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Toast.makeText(requireContext(),"Failed!",Toast.LENGTH_SHORT).show()
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Toast.makeText(requireContext(),"LOADED",Toast.LENGTH_SHORT).show()
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.edtUrl.requestFocus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonClassForAPI = CommonClassForAPI.getInstance(requireActivity())

        pDialog = ProgressDialog(requireContext())
        pDialog!!.setMessage("Please wait...")
        pDialog!!.setCancelable(false)


        binding.apply {
            edtUrl.setOnClickListener {

            }
            val clipBoardManager1 =
                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val copiedString1: String =
                clipBoardManager1.primaryClip?.getItemAt(0)?.text.toString()
            if (copiedString1.isNotEmpty()) {
                if (Patterns.WEB_URL.matcher(copiedString1).matches()) {
                    AlertDialog.Builder(context)
                        .setTitle("VIDDE Says!")
                        .setMessage("Are you want to use this url? \n $copiedString1") // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(
                            "Paste"
                        ) { dialog, which ->

                            edtUrl.setText(copiedString1)

                        }
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.ic_dialog_alert)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "No Link Found", Toast.LENGTH_SHORT).show()
            }

            btnDownload.setOnClickListener {
                val url = edtUrl.text.toString().trim()


                if (url.isNotEmpty() && url.length > 4) {
                    val version = Build.VERSION.SDK_INT
                    var manager= context?.getSystemService(Context.CONNECTIVITY_SERVICE)
                    if(manager?.equals(ConnectivityManager.EXTRA_NO_CONNECTIVITY) != true){
                        if (url.contains("instagram")) {

                            parentFragmentManager.beginTransaction().replace(
                                com.devphics.vidde.R.id.fragment_container,
                                instagram_fragment()
                            ).commit()


                        } else if (url.contains("twitter.com")) {

                            try {
                                val downloader = TwitterVideoDownloader(context, url)
                                downloader.DownloadVideo()
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }

                        } else if (url.contains("youtube.com") || url.contains("youtu.be")) {
                            Toast.makeText(context, "Youtube Link are not Allowed", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val intent = Intent(activity, WebActivity::class.java)
                            intent.putExtra("txturl", url)
                            startActivity(intent)

                        }
                    }else{
                        Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Toast.makeText(context, "Enter Valid Url", Toast.LENGTH_SHORT).show()
                }


            }
            containerInstagram.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(
                    com.devphics.vidde.R.id.fragment_container,
                    instagram_fragment()
                ).commit()
            }
            containerFacebook.setOnClickListener {
                startActivity(Intent(requireContext(), FacebookActivity::class.java))
            }

            containerTwitter.setOnClickListener {
                startActivity(Intent(requireContext(), TwitterActivity::class.java))

            }
            containerWhatsApp.setOnClickListener {

                startActivity(Intent(requireContext(), WhatsAppActivity::class.java))
            }
        }

    }









    val Overlay_REQUEST_CODE = 251

    fun checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(requireContext())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireContext().packageName)
                )
                startActivityForResult(intent, Overlay_REQUEST_CODE)
            } else {
                openFloatingWindow()
            }
        } else {
            openFloatingWindow()
        }
    }

    private fun openFloatingWindow() {
        val intent = Intent(requireContext(), FloatingWindow::class.java)
        requireContext().stopService(intent)
        requireContext().startService(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // if (requestCode == Activity.RESULT_OK) {

        when (requestCode) {
            Overlay_REQUEST_CODE -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(requireContext())) {
                        openFloatingWindow()
                    }
                } else {
                    openFloatingWindow()
                }
            }
        }

    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            try {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId === id) {

                    Toast.makeText(requireContext(), "Download Completed", Toast.LENGTH_LONG).show()
                }


            } finally {
            }


        }
    }


    @SuppressLint("Range")
    fun downloadVideo(url: String, filename: String) {

        try {
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            val downloadManager =
                requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "VIDDE" + " " + authorname
            )

            downloadId = downloadManager!!.enqueue(request)
            val dialogView =
                layoutInflater.inflate(com.devphics.vidde.R.layout.dialog_custom_layout, null)

            val customDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .show()


            val btDismiss =
                dialogView.findViewById<Button>(com.devphics.vidde.R.id.btDismissCustomDialog)
            val progressBar =
                dialogView.findViewById<ProgressBar>(com.devphics.vidde.R.id.progressBar1)
            val percentage = dialogView.findViewById<TextView>(com.devphics.vidde.R.id.percentageTA)
            btDismiss.setOnClickListener {
                customDialog.dismiss()
            }
            Thread {
                var downloading = true
                while (downloading) {
                    val q = DownloadManager.Query()
                    q.setFilterById(downloadId!!)
                    val cursor: Cursor = downloadManager.query(q)
                    cursor.moveToFirst()
                    if (cursor.count > 0) {
                        bytes_downloaded = cursor.getInt(
                            cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                        )

                    }
                    if (cursor.count > 0) {
                        val bytes_total: Int =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false
                        }
                        dl_progress = ((bytes_downloaded * 100f).toDouble() / bytes_total).toInt()
                    }

                    val handler = Handler(Looper.getMainLooper())

                    handler.post {
                        progressBar.progress = dl_progress.toInt()

                        val domain: String? = dl_progress.toString().substringBeforeLast(".")

                        percentage.text = domain + "%"

                    }

                    cursor.close()
                }
            }.start()
            binding.edtUrl.setText("")
            binding.edtUrl.requestFocus()
            hidepDialog()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }



    private fun showpDialog() {
        if (!pDialog!!.isShowing) pDialog!!.show()
    }

    private fun hidepDialog() {
        if (pDialog!!.isShowing) pDialog!!.dismiss()

    }

    private fun genNameFB(n: Int): String? {
        val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz")
        val sb = StringBuilder(n)
        for (i in 0 until n) {
            val index = (AlphaNumericString.length * Math.random()).toInt()
            sb.append(AlphaNumericString[index])
        }
        return sb.toString()
    }
}


