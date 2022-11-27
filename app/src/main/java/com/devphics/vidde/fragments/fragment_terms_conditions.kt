package com.devphics.vidde.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devphics.vidde.R



/**
 * A simple [Fragment] subclass.
 * Use the [fragment_terms_conditions.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_terms_conditions : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_conditions, container, false)
    }


}