package com.beratyesbek.Vhoops.Activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.beratyesbek.Vhoops.Core.Permission.GalleryPermission
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.databinding.ActivityProfileAcitivtyBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAcitivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileAcitivtyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.includeProfileActivity.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);


        binding.btnEditFirstName.setOnClickListener {
            showDialog("", "firstName")
        }
        binding.btnEditLastName.setOnClickListener {
            showDialog("", "lastName")

        }
        binding.btnEditUserName.setOnClickListener {
            showDialog("", "UserName")

        }
        binding.btnEditAbout.setOnClickListener {
            showDialog("", "Hakkında")

        }
        binding.btnSelectTool.setOnClickListener {
            showToolDialog()
        }

    }

    private fun showDialog(value: String, title: String) {
        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_dialog_item)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var textViewTitle = alertDialog.findViewById<TextView>(R.id.textView_custom_dialog_title)
        textViewTitle!!.text = title;

        alertDialog.show()
    }

    private fun showToolDialog() {
        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_gallery_dialog_item)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnGallery = alertDialog.findViewById<ImageButton>(R.id.btn_gallery_photo)
        val btnCamera = alertDialog.findViewById<ImageButton>(R.id.btn_camera_photo)
        val btnRemove = alertDialog.findViewById<ImageButton>(R.id.btn_delete_photo)

        btnGallery!!.setOnClickListener {
            GalleryPermission.selectImage(this,this);
        }
        btnCamera!!.setOnClickListener {
            val intentToCamera = Intent(this, CameraActivity::class.java);
            startActivity(intentToCamera)
        }
        btnRemove!!.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        println("0")

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        println("1")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("2")
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            println("3")

            val selectedPicture = data.data
            println("2" + selectedPicture.toString())

            try {
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, selectedPicture!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        println("2" + source.toString())

                    } else {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        println("2" + bitmap.toString())

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }



        super.onActivityResult(requestCode, resultCode, data)
    }


}