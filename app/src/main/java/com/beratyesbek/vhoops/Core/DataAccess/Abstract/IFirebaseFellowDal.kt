package com.beratyesbek.vhoops.Core.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.abstracts.IEntity

interface IFirebaseFellowDal<T : IEntity> : IEntityRepository<T> {
}