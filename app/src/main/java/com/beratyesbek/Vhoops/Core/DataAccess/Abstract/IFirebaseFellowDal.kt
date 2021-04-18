package com.beratyesbek.Vhoops.Core.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity

interface IFirebaseFellowDal<T : IEntity> : IEntityRepository<T> {
}