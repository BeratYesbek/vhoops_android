package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity
import com.beratyesbek.Vhoops.Entities.Concrete.Chat

interface IFirebaseChatDal<T : IEntity> : IEntityRepository<T> {
}