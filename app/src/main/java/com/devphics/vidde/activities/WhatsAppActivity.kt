package com.devphics.vidde.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devphics.vidde.R
import com.devphics.vidde.WhatsApp.WhatsAppFragments.BusinessMainWhatsAppFragment
import com.devphics.vidde.WhatsApp.WhatsAppFragments.SimpleMainWhatsAppFragment
import com.devphics.vidde.databinding.ActivityWhatsAppBinding


class WhatsAppActivity : AppCompatActivity() {


    companion object {
        lateinit var APP_NAME: String

    }

    private lateinit var binding: ActivityWhatsAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeStatusBarColor()



        binding.apply {

            imgBack.setOnClickListener {
                onBackClick()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                SimpleMainWhatsAppFragment()
            ).commit()

            toggleButtonGroup.setOnCheckedChangeListener { _, id ->
                var btn = findViewById<RadioButton>(id)

                when (btn) {
                    btnWhatsApp -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragmentContainer,
                            SimpleMainWhatsAppFragment()
                        ).commit()
                    }
                    btnWhatsAppBA -> {
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragmentContainer,
                            BusinessMainWhatsAppFragment()
                        ).commit()
                    }

                }
            }


        }
    }


    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.primary)
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
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    //  DownloadingImage();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    public fun onBackClick() {
        onBackPressed()

    }

    public fun getAppName(): String {
        return APP_NAME;
    }


}