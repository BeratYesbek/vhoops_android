package com.beratyesbek.Vhoops.Views.Fragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.DataAccess.UserDal
import com.beratyesbek.Vhoops.databinding.FragmentCameraBinding
import com.google.firebase.auth.FirebaseAuth


class CameraFragment(val bitmap: Bitmap,val uri : Uri,val documentId :String) : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(layoutInflater)
        val view = binding.root
        if(bitmap != null){
            binding.imageViewCameraFragment.setImageBitmap(bitmap)
        }


        binding.btnCancelCameraFragment.setOnClickListener {
            activity!!.onBackPressed()
        }



        binding.btnApproveCameraFragment.setOnClickListener {
            updateUserPofile()
        }

        return view;




    }

    private fun updateUserPofile(){
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val userDal : UserDal = UserDal()
        val userManager = UserManager(userDal)
        if(uri != null){
            userManager.uploadPhoto(uri){
                if(it.success()){
                    userManager.getPhoto(userId){
                        if(it.success()){

                            userManager.updateUserProfileImage(it.data(),documentId){result ->
                                if(result.success()){
                                    Toast.makeText(context,result.message(),Toast.LENGTH_LONG).show()
                                    activity!!.onBackPressed()
                                }else{
                                    Toast.makeText(context,result.message(),Toast.LENGTH_LONG).show()

                                }
                            }
                        }else{
                            Toast.makeText(context,it.message(),Toast.LENGTH_LONG).show()
                        }

                    }
                }else{
                    Toast.makeText(context,it.message(),Toast.LENGTH_LONG).show()

                }
            }

        }
    }


}