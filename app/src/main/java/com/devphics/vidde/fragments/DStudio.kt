package com.devphics.vidde.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devphics.vidde.databinding.FragmentDstudioBinding

class DStudio  : Fragment() {

    private lateinit var binding: FragmentDstudioBinding
    val REQUEST_CALL = 22
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDstudioBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            text2.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dstud.io?r=nametag"))
                startActivity(browserIntent)
            }
            text3.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.behance.net/dstudioo"))
                startActivity(browserIntent)
            }
            containerWhatsApp.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(
                        "http://api.whatsapp.com/send?phone=" + "+923324584006"
                    )
                    if (intent != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please Install WhatsApp",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            containerCall.setOnClickListener {
                makeCall()
            }
        }
    }

    private fun makeCall() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.CALL_PHONE
                ), REQUEST_CALL
            )
        } else {
            val phoneCallNumber = "+923324584006"
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneCallNumber")
            startActivity(intent)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}