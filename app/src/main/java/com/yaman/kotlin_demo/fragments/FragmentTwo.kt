package com.yaman.kotlin_demo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.yaman.kotlin_demo.R
import com.yaman.kotlin_demo.databinding.FragmentTwoBinding

class FragmentTwo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.e("FragmentTwo", "onCreateView: ")
        val binding =
            DataBindingUtil.inflate<FragmentTwoBinding>(inflater,
                R.layout.fragment_two,
                container,
                false)


        

        binding.secondFrag.setOnClickListener {
            findNavController().navigate(R.id.dialogFragment)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("FragmentTwo", "onCreate: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("FragmentTwo", "onAttach: ")
    }

    override fun onResume() {
        super.onResume()
        Log.e("FragmentTwo", "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("FragmentTwo", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("FragmentTwo", "onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("FragmentTwo", "onDestroyView: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("FragmentTwo", "onDestroy: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("FragmentTwo", "onDetach: ")
    }

}