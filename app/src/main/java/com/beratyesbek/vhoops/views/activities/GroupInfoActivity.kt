package com.beratyesbek.vhoops.views.activities

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.beratyesbek.vhoops.adapter.GroupInfoViewAdapter
import com.beratyesbek.vhoops.core.constants.Constants
import com.beratyesbek.vhoops.core.permission.GalleryPermission
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.core.utilities.extensions.downloadFromUrl
import com.beratyesbek.vhoops.mvvm.GroupInfoViewModel
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.databinding.ActivityGroupInfoBinding
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import com.beratyesbek.vhoops.views.fragment.GroupInfoAddUserFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupInfoActivity : AppCompatActivity(),OnItemClickListener {
    private lateinit var dataBinding: ActivityGroupInfoBinding
    private val viewModel: GroupInfoViewModel by viewModels()
    private lateinit var groupInfoViewAdapter: GroupInfoViewAdapter
    private lateinit var transaction: FragmentTransaction;
    private val currentUserId = FirebaseAuth.getInstance().currentUser.uid

    private var groupId = ""
    private val userList = ArrayList<User>()
    private lateinit var group: Group
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = ActivityGroupInfoBinding.inflate(layoutInflater)
        val view = dataBinding.root
        setContentView(view)
        groupId = intent.getStringExtra(Constants.GROUP_ID).toString()



        viewModel.getGroupData(groupId)
        dataBinding.btnAddMemberGroupInfo.isEnabled = false
        viewModel.groupLiveData.observe(this, {
            dataBinding.btnAddMemberGroupInfo.isEnabled = true
            group = it[0]
            runRecyclerView()
            checkAdmin()
            dataBinding.group = group
            dataBinding.imageViewCircleGroupInfo.downloadFromUrl(
                group?.groupImage.toString(),
                CircularProgressDrawable(this)
            )
        })
        viewModel.userLiveData.observe(this, { data ->
            userList.clear()
            userList.addAll(data)
            groupInfoViewAdapter.notifyDataSetChanged()
            dataBinding.recyclerViewGroupInfoFragment.scheduleLayoutAnimation()
        })

        viewModel.result.observe(this, {
            Toast.makeText(this, "Data was updated successfully", Toast.LENGTH_LONG).show()
        })

        dataBinding.btnBackGroupInfo.setOnClickListener {
            finish()
        }
        dataBinding.btnSeeGroupDescription.setOnClickListener {
            customDialog()
        }
        dataBinding.btnEditGroupName.setOnClickListener {
            showEditDialog(group?.groupName!!, "Group Name", 1)
        }
        dataBinding.btnSelectTool.setOnClickListener {
            GalleryPermission.selectImage(this, this)
        }
        dataBinding.btnAddMemberGroupInfo.setOnClickListener {
            runGroupInfoAddMemberFragment()
        }


    }
    private fun checkAdmin(){
        if(currentUserId != group?.adminId){
            dataBinding.btnAddMemberGroupInfo.visibility = View.GONE
            dataBinding.btnEditGroupName.visibility = View.GONE
            dataBinding.btnSelectTool.visibility = View.GONE

        }
    }

    private fun runRecyclerView() {
        val recyclerView = dataBinding.recyclerViewGroupInfoFragment
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        groupInfoViewAdapter = GroupInfoViewAdapter(userList,group,this)
        recyclerView.adapter = groupInfoViewAdapter
        recyclerView.refreshDrawableState()
        recyclerView.setItemAnimator(DefaultItemAnimator())
        Animation.listItemAnimationSecond(recyclerView)

    }

    private fun runGroupInfoAddMemberFragment(){

        transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
        transaction.replace(R.id.frameLayout_groupInfo_activity, GroupInfoAddUserFragment(group!!))
        transaction.commit()
    }

    private fun customDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_group_description)

        val textViewDescription = dialog.findViewById<TextView>(R.id.textView_group_description)
        val btnEdit = dialog.findViewById<Button>(R.id.btn_edit_groupDescription)
        val btnClose =
            dialog.findViewById<ImageButton>(R.id.btn_close_custom_groupDescriptionDialog)

        textViewDescription.text = group?.groupDescription
        btnEdit.setOnClickListener {
            showEditDialog(group?.groupDescription!!, "Group Description", 2)
        }
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showEditDialog(value: String, title: String, editTextId: Int) {
        val alertDialog = BottomSheetDialog(this, R.style.BottonSheetTheme)

        alertDialog.setContentView(R.layout.custom_dialog_item)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val textViewTitle = alertDialog.findViewById<TextView>(R.id.textView_custom_dialog_title)
        val editText = alertDialog.findViewById<EditText>(R.id.editText_customDialog)
        val editTextDescription =
            alertDialog.findViewById<EditText>(R.id.editText_customDialog_description)

        val buttonOk = alertDialog.findViewById<Button>(R.id.btn_save_customDialog)
        val buttonCancel = alertDialog.findViewById<Button>(R.id.btn_cancel_customDialog)

        editText!!.setText(value)
        editTextDescription?.setText(value)
        textViewTitle!!.text = title;


        if (editTextId == 2) {
            editTextDescription?.visibility = View.VISIBLE
            editText.visibility = View.INVISIBLE
        }

        buttonOk!!.setOnClickListener {

            val textValue = editText.text.toString()
            val textValueDescription = editTextDescription?.text.toString()
            if (editTextId == 2) {
                group?.groupDescription = textValueDescription
                viewModel.update(group!!)

            } else {
                group?.groupName = textValue
                viewModel.update(group!!)
            }


        }

        buttonCancel!!.setOnClickListener {
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
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode !== RESULT_CANCELED) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    viewModel.updateGroupImage(uri, group!!)

                }


            }
        }


    }

    override fun onItemClick(position: Int) {
        val user = userList[position]
        group?.memberIdList?.remove(user.userID)
        viewModel.update(group!!)
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }

}