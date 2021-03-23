package com.beratyesbek.Vhoops.Business.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Entities.Abstract.IEntity

interface IServiceRepository<T : IEntity> : IEntityRepository<T> {
}