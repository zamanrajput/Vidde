package com.devphics.vidde.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devphics.vidde.databinding.FragmentImageEditBinding

class ImageEditFragments : Fragment() {

    private lateinit var binding: FragmentImageEditBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageEditBinding.inflate(layoutInflater,container,false)

        return binding.root
    }
}