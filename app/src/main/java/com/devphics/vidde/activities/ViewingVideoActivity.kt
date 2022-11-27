package com.devphics.vidde.activities

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.devphics.vidde.databinding.ActivityViewingVideoBinding
import com.devphics.vidde.util.BasicData
import com.google.android.gms.ads.rewarded.RewardedAd
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


class ViewingVideoActivity : AppCompatActivity() {
    private var mRewardedAd: RewardedAd? = null
    private lateinit var binding: ActivityViewingVideoBinding
    private var MyFileList: String? = null
    private var check: String? = null
    private var newPath: String? = null
    private var type: String? = null
    private var WhatsapFolderLocation: String? = null


    private val REQ_COD = 111
    var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewingVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        activityResultLauncher = registerForActivityResult(
//            StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    if (Environment.isExternalStorageManager()) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Permissions Granted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        startActivity(Intent(applicationContext, MainActivity::class.java))
//                        this@ViewingVideoActivity.finish()
//                        Toast.makeText(
//                            applicationContext,
//                            "Permissions Granted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            applicationContext,
//                            "Permissions Not Granted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }

//
//        if (check.equals("yes")) {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("videoactivty", "yes")
//            startActivity(intent)
//
//        }


//        MobileAds.initialize(this) {}
//        var adRequest = AdRequest.Builder().build()
//        RewardedAd.load(this,"ca-app-pub-3736818881462482/5901347768", adRequest, object : RewardedAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                adError?.message?.let { Log.d(TAG, it) }
//                mRewardedAd = null
//            }
//
//            override fun onAdLoaded(rewardedAd: RewardedAd) {
//                Log.d(TAG, "Ad was loaded.")
//                mRewardedAd = rewardedAd
//            }
//        })

//        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d(TAG, "Ad was shown.")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                // Called when ad fails to show.
//                Log.d(TAG, "Ad failed to show.")
//            }
//
//            override fun onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d(TAG, "Ad was dismissed.")
//                mRewardedAd = null
//            }
//        }


//        Log.e("ETC_DATA_VANEWs", "type")
        type = intent.getStringExtra("type").toString()
        if (type.equals("Video_Adapter")) {
//            Log.e("ETC_DATA_VALUEs", "type")
            newPath = intent.getStringExtra("NEW_VIDEODATA").toString()

            binding.btnDownload.isVisible = false
            playVideo(newPath)
        }else if(type.equals("mp4")){
            binding.btnDownload.isVisible=false;
            playVideo(intent.getStringExtra("NEW_VIDEODATA").toString())
        }


        else {
            binding.apply {
                MyFileList = intent.getStringExtra("MyFilesList").toString()
                WhatsapFolderLocation = intent.getStringExtra("FolderLocation").toString()
                playVideo(MyFileList)

                binding.videoView.setOnCompletionListener { binding.videoView.seekTo(1) }

                btnDownload.setOnClickListener {

                    if(Build.VERSION.SDK_INT>=30){
                        var file = File(MyFileList.toString())


                        var result = addVideo(file)
                        if(result!=null){
                            Toast.makeText(applicationContext,"File Saved",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (DownloadVideo()) {
                            Toast.makeText(
                                this@ViewingVideoActivity,
                                "Video Download Completed",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnDownload.visibility = View.VISIBLE
                            this@ViewingVideoActivity.finish()
                        } else {
                            binding.btnDownload.visibility = View.VISIBLE
                            Toast.makeText(
                                this@ViewingVideoActivity,
                                "Video Download Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }




//                    if (mRewardedAd != null) {
////                        mRewardedAd?.show(this@ViewingVideoActivity, OnUserEarnedRewardListener() {
////                            if (CheckPermissions()) {
////                                if (ActivityCompat.checkSelfPermission(
////                                        this@ViewingVideoActivity,
////                                        permission.WRITE_EXTERNAL_STORAGE
////                                    ) != PackageManager.PERMISSION_GRANTED
////                                ) {
////
////                                } else {
////                                    if (DownloadVideo()) {
////                                        Toast.makeText(
////                                            this@ViewingVideoActivity,
////                                            "Video Download Completed",
////                                            Toast.LENGTH_SHORT
////                                        ).show()
////                                        binding.btnDownload.visibility = View.VISIBLE
////                                        this@ViewingVideoActivity.finish()
////                                    } else {
////                                        binding.btnDownload.visibility = View.VISIBLE
////                                        Toast.makeText(
////                                            this@ViewingVideoActivity,
////                                            "Video Download Failed",
////                                            Toast.LENGTH_SHORT
////                                        ).show()
////                                    }
////                                }
////                            } else {
////                                requestPermissions()
////                            }
////                        })
//                    } else {
//
//                    }
                }

            }
        }
    }


    private fun playVideo(path: String?) {
        Log.e("VIDEO_PATH_VALUES", "VALUES" + path)
        binding.videoView.setVideoURI(Uri.parse(path))
        val mediaController = MediaController(this@ViewingVideoActivity)
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)

        binding.videoView.seekTo(0)
        binding.videoView.start()
    }


    private fun CheckPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val ReadCheck = ContextCompat.checkSelfPermission(
                applicationContext,
                permission.READ_EXTERNAL_STORAGE
            )
            val WriteCheck = ContextCompat.checkSelfPermission(
                applicationContext,
                permission.WRITE_EXTERNAL_STORAGE
            )
            ReadCheck == PackageManager.PERMISSION_GRANTED && WriteCheck == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun RequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                activityResultLauncher?.launch(intent)
            } catch (e: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                activityResultLauncher?.launch(intent)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions =
                    arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(
                    this@ViewingVideoActivity,
                    permissions,
                    REQ_COD
                )
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {

        if (requestCode == REQ_COD) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permissions Granted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, "Permissions Not Granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    fun addVideo(videoFile: File): Uri? {
        val values = ContentValues(3)
        values.put(MediaStore.Video.Media.TITLE, videoFile.name)
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        values.put(MediaStore.Video.Media.DATA, videoFile.absolutePath)
        return contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun DownloadVideo(): Boolean {
        binding.btnDownload.visibility = View.INVISIBLE
        val MySdCardDirectory =
            File(
                BasicData.getInternalStorageDirectoryPath(applicationContext),
                WhatsapFolderLocation
            )
        if (!MySdCardDirectory.exists()) {
            MySdCardDirectory.mkdirs()
        }
        val MyVideo = File(MySdCardDirectory, "/Video" + System.currentTimeMillis() + ".mp4")


        var list = File(
            BasicData.getInternalStorageDirectoryPath(applicationContext)
                .toString() + File.separator + "/VIDDE/Status Saver/SavedVideos"
        ).listFiles()
        var FLAG = false
        for (f in list) {
            if (f.name == MyVideo.name) {
                FLAG = true
            }

        }


        var success = false
        val outputStream: FileOutputStream
        if (!FLAG) {
            try {
                val inputStream: InputStream = FileInputStream(MyFileList)
                outputStream = FileOutputStream(MyVideo)
                val buf = ByteArray(1024)
                var length: Int
                while (inputStream.read(buf).also { length = it } > 0) {
                    outputStream.write(buf, 0, length)
                }
                inputStream.close()
                outputStream.close()
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                values.put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                values.put(MediaStore.MediaColumns.DATA, MyVideo.absolutePath)
                this@ViewingVideoActivity.contentResolver.insert(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                success = true
            } catch (e: Exception) {
                Log.e("Exception", e.message!!)
            }


        } else {
            success = false
            Toast.makeText(applicationContext, "Already Available", Toast.LENGTH_SHORT).show()
        }
        return success
    }

    override fun onBackPressed() {
        super.onBackPressed()
        check = "yes"

//        startActivity(Intent(this, MainActivity::class.java))

//    val intent = Intent(this, MainActivity::class.java)
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.putExtra("videoactivty", "yes")
//        startActivity(intent)
//        finish()

//        supportFragmentManager.beginTransaction()
//            .add(android.R.id.content, PasDownloadsFragment()).commit()
//        startActivity(intent)


//        finish()


    }


}



