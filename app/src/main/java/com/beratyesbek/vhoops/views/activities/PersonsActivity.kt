package com.beratyesbek.vhoops.views.activities

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.beratyesbek.vhoops.adapter.gridViewAdapter.GroupMemberViewAdapter
import com.beratyesbek.vhoops.adapter.PersonViewAdapter
import com.beratyesbek.vhoops.business.concretes.GroupManager
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.permission.GalleryPermission
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.dataAccess.concretes.GroupDal
import com.beratyesbek.vhoops.mvvm.PersonsViewModel
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.ActivityPersonsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PersonsActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var dataBinding: ActivityPersonsBinding

    private val userList: ArrayList<User> = ArrayList()

    private val selectedItemList: ArrayList<Number> = ArrayList()
    private val selectedUserList: ArrayList<User> = ArrayList()
    private val selectedUserId : ArrayList<String> = ArrayList()

    private lateinit var transaction: FragmentTransaction;

    private lateinit var personViewAdapter: PersonViewAdapter
    private lateinit var groupMembersViewAdapter: GroupMemberViewAdapter

    private var groupIcon: Uri? = null

    private val viewModel : PersonsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityPersonsBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        setSupportActionBar(dataBinding.toolbarPersonsActivity.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        dataBinding.toolbarPersonsActivity.btnToolbarNewGroup.setOnClickListener {
            if (selectedUserList.size > 0) {
                groupCreationDialog()
            }
        }

        dataBinding.toolbarPersonsActivity.btnBackToolbar.setOnClickListener {
            onBackPressed()
        }

        runRecyclerView()
        viewModel.getFriendData()
        viewModel.friendList.observe( this,{ friendList ->
            userList.clear()
            userList.addAll(friendList)
            personViewAdapter.notifyDataSetChanged()

        } )


    }


    private fun runRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewFriendActivity.layoutManager = layoutManager
        personViewAdapter = PersonViewAdapter(userList, this)
        dataBinding.recyclerViewFriendActivity.adapter = personViewAdapter
    }

    private fun runGridView(gridView: GridView) {
        groupMembersViewAdapter = GroupMemberViewAdapter(selectedUserList, this)
        gridView.adapter = groupMembersViewAdapter
        groupMembersViewAdapter.notifyDataSetChanged()

    }

    private fun groupCreationDialog() {
        val dialog = BottomSheetDialog(this, R.style.BottonSheetTheme)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_group_dialog)

        val gridView = dialog.findViewById<GridView>(R.id.gridView_groupMembers)
        val btnChoseImage = dialog.findViewById<ImageButton>(R.id.btn_select_group_icon)
        val btnCreate = dialog.findViewById<ImageButton>(R.id.btn_create_group)
        val editTextGroupName = dialog.findViewById<EditText>(R.id.editText_groupName_groupDialog)
        val editTextGroupDescription = dialog.findViewById<EditText>(R.id.editText_groupDescription_groupDialog)


        btnChoseImage?.setOnClickListener {
            GalleryPermission.selectImage(this, this)

        }
        btnCreate?.setOnClickListener {
            val groupName = editTextGroupName?.text.toString()
            val description = editTextGroupDescription?.text.toString()
            if(!groupName.isNullOrEmpty() && !description.isNullOrEmpty()){
                createGroup(groupName,description,dialog)

            }

        }

        runGridView(gridView!!)

        dialog.show()

    }

    private fun createGroup(groupName: String,groupDescription : String ,dialog: Dialog) {

        if (selectedUserList.size > 0 && groupIcon != null){

            val groupManager = GroupManager(GroupDal())

            groupManager.uploadGroupIcon(groupIcon!!) { iDataResult ->
                if (iDataResult.success()){
                    val adminId = FirebaseAuth.getInstance().currentUser.uid
                    val uuid = UUID.randomUUID().toString()
                    groupManager.add(Group(uuid,groupName,groupDescription,adminId,iDataResult.data(),uuid,selectedUserId, Timestamp.now())){
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




    override fun onItemClick(position: Int) {
        if (selectedItemList.size > 0) {
            selectedItemList.forEachIndexed { index, number ->
                if (number == position) {
                    selectedItemList.removeAt(index)
                    selectedUserList.removeAt(index)
                    selectedUserId.removeAt(index)

                }
            }
        } else {
            val user = userList[position]

            val intentToChatActivity = Intent(this, ChatActivity::class.java)
            intentToChatActivity.putExtra(Constants.FULL_NAME,user.firstName + " " + user.lastName)
            intentToChatActivity.putExtra(Constants.FIRST_NAME,user.firstName)
            intentToChatActivity.putExtra(Constants.LAST_NAME,user.lastName)
            intentToChatActivity.putExtra(Constants.USER_ID, user.userID)
            intentToChatActivity.putExtra(Constants.PROFILE_IMAGE, user.profileImage.toString())
            intentToChatActivity.putExtra(Constants.TOKEN,user.token)

            startActivity(intentToChatActivity)
        }
        if (selectedItemList.size <= 0) {
            selectedUserList.clear()
            selectedItemList.clear()
            selectedUserId.clear()

            Animation.hideAnim(dataBinding.toolbarPersonsActivity.btnToolbarNewGroup)
        }


    }

    override fun onItemLongClick(position: Int) {
        selectedItemList.add(position)
        val user = userList.get(position)
        selectedUserList.add(user)
        selectedUserId.add(user.userID)
        if (selectedItemList.size in 1..1) {
            Animation.revealAnim(dataBinding.toolbarPersonsActivity.btnToolbarNewGroup)
        }
    }

}