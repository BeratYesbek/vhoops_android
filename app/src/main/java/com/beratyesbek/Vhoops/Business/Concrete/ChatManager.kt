package com.beratyesbek.Vhoops.Business.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Business.Abstract.IChatService
import com.beratyesbek.Vhoops.Business.Abstract.IUserService
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.Vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.Vhoops.Entities.Concrete.Chat
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto

class ChatManager(val chatDal: IChatDal, val userService: IUserService) : IChatService {

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

    override fun getChatDetail(id: String, iDataResult: (IDataResult<ArrayList<ChatDto>>) -> Unit) {

        chatDal.getChatDetail(id) { iDataResult ->
            if (iDataResult.success()) {

                userService.getById(id) { result ->
                    val user = result.data().get(0)
                    val chatDto = iDataResult.data().get(0)

                    chatDto.userFullName = user.firstName + user.lastName
                    chatDto.userPicture = user.profileImage!!

                    iDataResult(SuccessDataResult(iDataResult.data(), ""))

                }
            }
        }

    }


}