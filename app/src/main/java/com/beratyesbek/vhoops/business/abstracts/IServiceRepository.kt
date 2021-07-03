package com.beratyesbek.vhoops.business.abstracts

import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.abstracts.IEntity

interface IServiceRepository<T : IEntity> : IEntityRepository<T> {
}