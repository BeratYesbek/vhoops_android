package com.beratyesbek.vhoops.Business.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Business.Abstract.IGroupChatService
import com.beratyesbek.vhoops.Business.Abstract.IUserService
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IGroupChatDal
import com.beratyesbek.vhoops.entities.concrete.dtos.GroupChatDto
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import javax.inject.Inject

class GroupChatManager
@Inject
constructor(private val groupChatDal: IGroupChatDal, private val userService: IUserService) :
    IGroupChatService {


    override fun add(entity: GroupChat, result: (IResult) -> Unit) {
        groupChatDal.add(entity, result)
    }

    override fun update(entity: GroupChat, result: (IResult) -> Unit) {
        groupChatDal.update(entity, result)
    }

    override fun delete(entity: GroupChat, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<GroupChat>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<GroupChat>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getChatDetailById(
        id: String,
        iDataResult: (IDataResult<ArrayList<GroupChatDto>>) -> Unit
    ) {
        groupChatDal.getChatDetailById(id, iDataResult)
    }

    override fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit) {
       groupChatDal.uploadFile(uri,type,result)
    }

    override fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        groupChatDal.getFile(path,iDataResult)
    }
}