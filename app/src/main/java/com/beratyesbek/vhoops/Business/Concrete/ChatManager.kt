package com.beratyesbek.vhoops.Business.Concrete

import android.net.Uri
import com.beratyesbek.vhoops.Business.Abstract.IChatService
import com.beratyesbek.vhoops.Business.Abstract.IUserService
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.vhoops.entities.concrete.Chat
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatListDto
import javax.inject.Inject

class ChatManager
@Inject constructor(val chatDal: IChatDal, val userService: IUserService) :
    IChatService {

    override fun add(entity: Chat, result: (IResult) -> Unit) {
        chatDal.add(entity, result)
    }

    override fun update(entity: Chat, result: (IResult) -> Unit) {
        chatDal.update(entity, result)
    }

    override fun delete(entity: Chat, result: (IResult) -> Unit) {
        chatDal.delete(entity, result)
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        chatDal.getAll(iDataResult)
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<Chat>>) -> Unit) {
        chatDal.getById(id, iDataResult)
    }

    override fun uploadFile(uri: Uri, type: String, result: (IDataResult<String>) -> Unit) {
        chatDal.uploadFile(uri, type, result)
    }

    override fun getFile(path: String, iDataResult: (IDataResult<Uri>) -> Unit) {
        chatDal.getFile(path, iDataResult)
    }

    override fun deleteMulti(arrayList: ArrayList<Chat>, result: (IResult) -> Unit) {
        chatDal.deleteMulti(arrayList, result)
    }

    override fun getChatDetailForList(
        id: String,
        iDataResult: (IDataResult<ArrayList<ChatListDto>>) -> Unit
    ) {
        chatDal.getChatDetailForList(id, iDataResult)
    }

    override fun getChatDetail(id: String, iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit) {

        chatDal.getChatDetail(id) { iDataResult ->
            if (iDataResult.success()) {

                userService.getById(id) { result ->
                    val user = result.data().get(0)
                    val chatDto = iDataResult.data().get(0)

                    chatDto.userFullName = user.firstName + user.lastName
                    chatDto.userPicture = user.profileImage

                    iDataResult(SuccessDataResult(iDataResult.data(), ""))

                }
            }
        }

    }


}