package com.beratyesbek.vhoops.views.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.Core.Constants.Constants
import com.beratyesbek.vhoops.Core.Permission.GalleryPermission
import com.beratyesbek.vhoops.Core.Utilities.Extension.downloadFromUrl
import com.beratyesbek.vhoops.Core.Utilities.Extension.placeHolderProgressBar
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.views.fragment.CameraFragment
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.databinding.ActivityProfileAcitivtyBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.toolbar.view.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivityProfileAcitivtyBinding
    private lateinit var user: User
    private lateinit var userDal: UserDal
    private lateinit var userManager: UserManager
    private lateinit var transaction: FragmentTransaction;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = ActivityProfileAcitivtyBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)

        setSupportActionBar(dataBinding.includeProfileActivity.toolbar)
        getData()

        dataBinding.btnEditFirstName.setOnClickListener {
            showDialog(user.firstName!!, "İsim", 1)
        }
        dataBinding.btnEditLastName.setOnClickListener {
            showDialog(user.lastName!!, "Soyisim", 2)

        }
        dataBinding.btnEditUserName.setOnClickListener {
            showDialog(user.userName!!, "Kullanıcı Adı", 3)

        }
        dataBinding.btnEditAbout.setOnClickListener {
            if (user.about != null) {
                showDialog(user.about!!, "Hakkında", 4)

            } else {
                showDialog("", "Hakkında", 4)

            }

        }
        dataBinding.btnSelectTool.setOnClickListener {
            showToolDialog()
        }

    }

    private fun getData() {
        val id = FirebaseAuth.getInstance().currentUser.uid
        val userDal: UserDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getById(id) { dataResult ->
            if (dataResult.success()) {
                user = dataResult.data().get(0);
                dataBinding.userData = user
                dataBinding.imageViewProfileActivity.downloadFromUrl(
                    user.profileImage.toString(),
                    placeHolderProgressBar(this)
                )
            }
        }
    }

    private fun showDialog(value: String, title: String, editTextId: Int) {
        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_dialog_item)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textViewTitle = alertDialog.findViewById<TextView>(R.id.textView_custom_dialog_title)
        val editText = alertDialog.findViewById<EditText>(R.id.editText_customDialog)
        val buttonOk = alertDialog.findViewById<Button>(R.id.btn_save_customDialog)
        val buttonCancel = alertDialog.findViewById<Button>(R.id.btn_cancel_customDialog)

        editText!!.setText(value)
        textViewTitle!!.text = title;


        buttonOk!!.setOnClickListener {

            val textValue = editText.text.toString()
            if (editTextId == 3) {
                updateUserName(textValue)
            } else {
                updateData(textValue, editTextId)
            }

        }

        buttonCancel!!.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun updateUserName(userName: String) {
        val documentId = user.documentID
        userDal = UserDal()
        userManager = UserManager(userDal)
        userManager.updateUserName(userName, documentId) { result ->
            if (result.success()) {
                Toast.makeText(this, result.message(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.message(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateData(textValue: String, editTextId: Int) {
        when (editTextId) {
            1 -> user.firstName = textValue
            2 -> user.lastName = textValue
            4 -> user.about = textValue

        }
        userDal = UserDal()
        userManager = UserManager(userDal)
        userManager.update(user) { result ->

            if (result.success()) {
                Toast.makeText(this, result.message(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.message(), Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun showToolDialog() {
        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_gallery_dialog_item)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnGallery = alertDialog.findViewById<ImageButton>(R.id.btn_gallery_photo)
        val btnCamera = alertDialog.findViewById<ImageButton>(R.id.btn_camera_photo)
        val btnRemove = alertDialog.findViewById<ImageButton>(R.id.btn_delete_photo)

        btnGallery!!.setOnClickListener {
            GalleryPermission.selectImage(this, this);
            alertDialog.dismiss()
        }
        btnCamera!!.setOnClickListener {
            val intentToCamera = Intent(this, CameraActivity::class.java)
            intentToCamera.putExtra("documentId", user.documentID)
            startActivity(intentToCamera)
        }
        btnRemove!!.setOnClickListener {
            userDal = UserDal()
            userManager = UserManager(userDal)
            userManager.removeProfileImage {

                if (it.success()) {
                    userManager.updateUserProfileImage(null, user.documentID) {
                        if (it.success()) {
                            Toast.makeText(this, "has been removed successfully", Toast.LENGTH_LONG)
                                .show()

                        }
                    }
                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imageSource: Uri? = null
        var bitmap: Bitmap? = null

        if (resultCode != RESULT_CANCELED) {
            if (data != null) {
                imageSource = data.data!!
                if (Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(contentResolver, imageSource!!)
                    bitmap = ImageDecoder.decodeBitmap(source)

                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageSource)

                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
        if (bitmap != null && imageSource != null) {
            runCameraFragment(imageSource)
        }
    }

    override fun onBackPressed() {

        if (supportFragmentManager.findFragmentById(R.id.frameLayout_profile_activity) != null) {
            removeCameraFragment()
        } else {
            super.onBackPressed()

        }
    }

    private fun runCameraFragment(uri: Uri) {

        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.replace(R.id.frameLayout_profile_activity,CameraFragment( uri,null, null,user.documentID,Constants.PROFILE_ACTIVITY))
        transaction.commit()
    }

    private fun removeCameraFragment() {
        transaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.frameLayout_profile_activity)
        transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in_anim)
        transaction.remove(fragment!!)
        transaction.commit()
    }


}