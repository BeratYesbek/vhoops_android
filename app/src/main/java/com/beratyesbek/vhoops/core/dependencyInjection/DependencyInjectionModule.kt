package com.beratyesbek.vhoops.core.dependencyInjection

import com.beratyesbek.vhoops.business.abstracts.*
import com.beratyesbek.vhoops.business.concretes.*
import dagger.Module

import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import com.beratyesbek.vhoops.dataAccess.abstracts.*
import com.beratyesbek.vhoops.dataAccess.concretes.*
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DependencyInjectionModule {

    @Singleton
    @Provides
    fun groupChatDalProvider(): IGroupChatDal {
        return GroupChatDal()
    }

    @Singleton
    @Provides
    fun groupChatManagerProvider(): IGroupChatService {
        return GroupChatManager(groupChatDalProvider(), userManagerProvider())
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

    @Singleton
    @Provides
    fun invitationDalProvider(): IInvitationDal {
        return InvitationDal()
    }

    @Singleton
    @Provides
    fun invitationManagerProvider() : IInvitationService {
        return InvitationManager(invitationDalProvider())
    }

    @Singleton
    @Provides
    fun friendRequestManagerProvider() : IFriendRequestService{
        return  FriendRequestManager(friendRequestDalProvider())
    }

    @Singleton
    @Provides
    fun friendRequestDalProvider() : IFriendRequestDal {
        return FriendRequestDal()
    }

    @Singleton
    @Provides
    fun fellowManagerProvider() : IFellowService{
        return FellowManager(fellowDalProvider())
    }

    @Singleton
    @Provides
    fun fellowDalProvider() : IFellowDal{
        return FellowDal()
    }

    @Singleton
    @Provides
    fun groupDalProvider() : IGroupDal{
        return GroupDal()
    }

    @Singleton
    @Provides
    fun groupManagerProvider() : IGroupService{
        return GroupManager(groupDalProvider())
    }

}