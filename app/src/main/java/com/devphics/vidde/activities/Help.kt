package com.devphics.vidde.activities

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.devphics.vidde.R
import com.devphics.vidde.databinding.ActivityHelpBinding

class Help : AppCompatActivity() {

    lateinit var binding:ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mediaController: MediaController= MediaController(this)
        binding.videoViewDialog.setMediaController(mediaController)

        mediaController.setAnchorView(binding.videoViewDialog)
        val path = "android.resource://" + packageName + "/" + R.raw.how_to_allow_permissions1
        val uri = Uri.parse(path)
        binding.videoViewDialog.setVideoURI(uri)
        binding.videoViewDialog.seekTo(0)
        binding.videoViewDialog.start()
        binding.videoViewDialog.setOnCompletionListener { onBackPressed() }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.videoViewDialog.isPlaying){
            binding.videoViewDialog.stopPlayback()
        }
    }

}