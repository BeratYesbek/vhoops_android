package com.beratyesbek.vhoops.Core.DependencyInjection

import android.app.Application
import com.beratyesbek.vhoops.Business.Abstract.IChatService
import com.beratyesbek.vhoops.Business.Abstract.IGroupChatService
import com.beratyesbek.vhoops.Business.Abstract.IUserService
import com.beratyesbek.vhoops.Business.Concrete.ChatManager
import com.beratyesbek.vhoops.Business.Concrete.GroupChatManager
import com.beratyesbek.vhoops.Business.Concrete.UserManager
import com.beratyesbek.vhoops.DataAccess.Concrete.GroupChatDal
import com.beratyesbek.vhoops.entities.concrete.GroupChat
import dagger.Module
import dagger.hilt.android.components.ActivityComponent

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import javax.inject.Singleton
import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.DataAccess.Abstract.IChatDal
import com.beratyesbek.vhoops.DataAccess.Abstract.IGroupChatDal
import com.beratyesbek.vhoops.DataAccess.Abstract.IUserDal
import com.beratyesbek.vhoops.DataAccess.Concrete.ChatDal
import com.beratyesbek.vhoops.DataAccess.Concrete.UserDal
import com.beratyesbek.vhoops.entities.abstracts.IEntity
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DependencyInjectionModule {

    @Singleton
    @Provides
    fun groupDalProvider(): IGroupChatDal {
        return GroupChatDal()
    }

    @Singleton
    @Provides
    fun groupManagerProvider(): IGroupChatService {
        return GroupChatManager(groupDalProvider(), userManagerProvider())
    }

    @Singleton
    @Provides
    fun userDalProvider(): IUserDal {
        return UserDal()
    }

    @Singleton
    @Provides
    fun userManagerProvider(): IUserService {
        return UserManager(userDalProvider())
    }

    @Singleton
    @Provides
    fun chatManagerProvider(): IChatService {
        return ChatManager(chatDalProvider(), userManagerProvider())
    }

    @Singleton
    @Provides
    fun chatDalProvider(): IChatDal {
        return ChatDal()
    }


}