package com.beratyesbek.Vhoops.Views.Activities

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.Vhoops.Adapter.GridViewAdapter.GroupMemberViewAdapter
import com.beratyesbek.Vhoops.Adapter.PersonViewAdapter
import com.beratyesbek.Vhoops.Business.Concrete.FellowManager
import com.beratyesbek.Vhoops.Business.Concrete.GroupManager
import com.beratyesbek.Vhoops.Business.Concrete.UserManager
import com.beratyesbek.Vhoops.Core.Constants.Constants
import com.beratyesbek.Vhoops.Core.Permission.GalleryPermission
import com.beratyesbek.Vhoops.Core.Utilities.Animation.Animation
import com.beratyesbek.Vhoops.DataAccess.Concrete.FellowDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.GroupDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow
import com.beratyesbek.Vhoops.Entities.Concrete.Group
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.ViewUtilities.OnItemClickListener
import com.beratyesbek.Vhoops.databinding.ActivityPersonsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


class PersonsActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityPersonsBinding

    private val userList: ArrayList<User> = ArrayList()
    private val fellowList: ArrayList<Fellow> = ArrayList()
    private val selectedItemList: ArrayList<Number> = ArrayList()
    private val selectedUserList: ArrayList<User> = ArrayList()
    private val selectedUserId : ArrayList<String> = ArrayList()

    private lateinit var personViewAdapter: PersonViewAdapter
    private lateinit var groupMembersViewAdapter: GroupMemberViewAdapter

    private var groupIcon: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPersonsActivity.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        binding.toolbarPersonsActivity.btnToolbarNewGroup.setOnClickListener {
            if (selectedUserList.size > 0) {
                groupCreationDialog()
            }
        }


        runRecyclerView()
        getFriendData()


    }

    private fun runGridView(gridView: GridView) {
        groupMembersViewAdapter = GroupMemberViewAdapter(selectedUserList, this)
        gridView.adapter = groupMembersViewAdapter
        groupMembersViewAdapter.notifyDataSetChanged()

    }

    private fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFriendActivity.layoutManager = layoutManager
        personViewAdapter = PersonViewAdapter(userList, fellowList, this)
        binding.recyclerViewFriendActivity.adapter = personViewAdapter
    }

    private fun groupCreationDialog() {
        val dialog = BottomSheetDialog(this, R.style.BottonSheetTheme)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_group_dialog)

        val gridView = dialog.findViewById<GridView>(R.id.gridView_groupMembers)
        val btnChoseImage = dialog.findViewById<ImageButton>(R.id.btn_select_group_icon)
        val btnCreate = dialog.findViewById<ImageButton>(R.id.btn_create_group)
        val editTextGroupName = dialog.findViewById<EditText>(R.id.editText_groupName_groupDialog)


        btnChoseImage?.setOnClickListener {
            GalleryPermission.selectImage(this, this)

        }
        btnCreate?.setOnClickListener {
            val groupName = editTextGroupName?.text.toString()
            createGroup(groupName,dialog)

        }

        runGridView(gridView!!)

        dialog.show()

    }

    private fun createGroup(groupName: String,dialog: Dialog) {

        if (selectedUserList.size > 0 && groupIcon != null){

            val groupManager = GroupManager(GroupDal())

            groupManager.uploadGroupIcon(groupIcon!!) { iDataResult ->
                if (iDataResult.success()){
                    val adminId = FirebaseAuth.getInstance().currentUser.uid
                    groupManager.add(Group(null,groupName,adminId,iDataResult.data(),null,selectedUserId, Timestamp.now())){
                        if (it.success()){
                            Toast.makeText(this,"Group has been created",Toast.LENGTH_LONG).show()
                            dialog.dismiss()

                        }else{
                            Toast.makeText(this,"Group hasn't been created",Toast.LENGTH_LONG).show()
                            dialog.dismiss()

                        }
                    }
                }else{
                    Toast.makeText(this,"Group hasn't been created",Toast.LENGTH_LONG).show()
                    dialog.dismiss()

                }
            }

          }

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


        if (resultCode != RESULT_CANCELED) {
            if (data != null) {

                groupIcon = data.data

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun getFriendData() {
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val fellowDal = FellowDal()
        val fellowManager = FellowManager(fellowDal)
        fellowManager.getById(userId) { iDataResult ->
            fellowList.clear()
            if (iDataResult.success()) {

                fellowList.addAll(iDataResult.data())
                getUserData()
            }
        }
    }


    private fun getUserData() {
        val userDal = UserDal()
        val userManager = UserManager(userDal)
        userManager.getAll { iDataResult ->
            userList.clear()
            if (iDataResult.success()) {
                userList.addAll(iDataResult.data())
                setAllDataByFriend()
            }
        }
    }

    private fun setAllDataByFriend() {
        val tempUserList = ArrayList<User>()
        println(fellowList.size)
        for (friend in fellowList) {

            for (user in userList) {
                if (friend.userId == user.userID) {
                    println(12385)
                    tempUserList.add(user)
                }
            }
        }
        userList.clear()
        userList.addAll(tempUserList)
        println(userList.size)
        personViewAdapter.notifyDataSetChanged()

    }

    override fun onItemClick(position: Int) {
        if (selectedItemList.size > 0) {
            selectedItemList.forEachIndexed { index, number ->
                println(number)
                if (number == position) {
                    selectedItemList.removeAt(index)
                    selectedUserList.removeAt(index)
                    selectedUserId.removeAt(index)

                }
            }
        } else {
            val user = userList.get(position)

            val intentToChatActivity = Intent(this, ChatActivity::class.java)
            intentToChatActivity.putExtra(
                Constants.FULL_NAME,
                (user.firstName + " " + user.lastName)
            )
            intentToChatActivity.putExtra(Constants.USER_ID, user.userID)
            intentToChatActivity.putExtra(Constants.PROFILE_IMAGE, user.profileImage.toString())

            startActivity(intentToChatActivity)
        }
        if (selectedItemList.size <= 0) {
            selectedUserList.clear()
            selectedItemList.clear()
            selectedUserId.clear()

            Animation.hideAnim(binding.toolbarPersonsActivity.btnToolbarNewGroup)
        }


    }

    override fun onItemLongClick(position: Int) {
        selectedItemList.add(position)
        val user = userList.get(position)
        selectedUserList.add(user)
        selectedUserId.add(user.userID)
        if (selectedItemList.size in 1..1) {
            Animation.revealAnim(binding.toolbarPersonsActivity.btnToolbarNewGroup)
        }
    }

}