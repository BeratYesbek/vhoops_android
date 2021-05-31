package com.beratyesbek.vhoops.Business.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.abstracts.IEntity

interface IServiceRepository<T : IEntity> : IEntityRepository<T> {
}