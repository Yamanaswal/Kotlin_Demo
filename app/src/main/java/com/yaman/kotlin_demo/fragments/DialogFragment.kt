package com.yaman.kotlin_demo.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yaman.kotlin_demo.R
import com.yaman.kotlin_demo.databinding.FragmentDialogBinding
import android.content.Intent

import android.app.Activity.RESULT_OK
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception
import android.provider.MediaStore


class FragmentDialog : DialogFragment() {

    val REQUEST_GET_SINGLE_FILE: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentDialogBinding>(inflater,
            R.layout.fragment_dialog,
            container,
            false)

        binding.filePicker.setOnClickListener {
            openFilePicker()
        }



        return binding.root
    }

    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
        }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
            REQUEST_GET_SINGLE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode === RESULT_OK) {
                if (requestCode === REQUEST_GET_SINGLE_FILE) {
                    var selectedImageUri: String? = data?.data?.path
                    Log.e("TAG", "onActivityResult: $selectedImageUri")

                    // Get the path from the Uri
                    val path: String = getPathFromURI(data?.data)
                    Log.e("TAG", "pathFromURI: $path")
//                    if (path != null) {
//                        val f = File(path)
//                        selectedImageUri = Uri.fromFile(f)
//                    }
                    // Set the image in ImageView
//                    ImageView(findViewById(R.id.imgView) as ImageView?).setImageURI(selectedImageUri)
                }
            }
        } catch (e: Exception) {
            Log.e("FileSelectorActivity", "File select error", e)
        }
    }


    private fun getPathFromURI(uri: Uri?): String {
        var path = ""
        if (context?.contentResolver != null) {
            val cursor = context?.contentResolver!!.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }
}