package com.beratyesbek.vhoops.mvvm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.beratyesbek.vhoops.business.abstracts.IGroupService
import com.beratyesbek.vhoops.business.abstracts.IUserService
import com.beratyesbek.vhoops.entities.concrete.Group
import com.beratyesbek.vhoops.entities.concrete.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupInfoViewModel
@Inject constructor(
    application: Application,
    private val groupService: IGroupService,
    private val userService: IUserService,
) :
    BaseViewModel(application) {

    val groupLiveData = MutableLiveData<ArrayList<Group>>()
    val userLiveData = MutableLiveData<ArrayList<User>>()
    val result = MutableLiveData<Boolean>()
    private val userList = ArrayList<User>()

    fun getGroupData(groupId: String) {
        groupService.getById(groupId) { iDataResult ->

            groupLiveData.value = iDataResult.data()
            getUserData(iDataResult.data()?.get(0))
        }
    }

    private fun getUserData(group: Group?) {
        userService.getAll { iDataResult ->
            for (value in group?.memberIdList!!) {

                for (user in iDataResult.data()!!)
                    if (user.userID.equals(value)) {
                        userList.add(user)
                        break;
                    }
            }
            userLiveData.value = userList
        }
    }

    fun update(group: Group) {
        groupService.update(group) {
            result.value = it.success()
        }
    }

    fun updateGroupImage(uri: Uri, group: Group) {
        groupService.uploadGroupIcon(uri) { iDataResult ->
            group.groupImage = iDataResult.data()
            update(group)
        }
    }


}
