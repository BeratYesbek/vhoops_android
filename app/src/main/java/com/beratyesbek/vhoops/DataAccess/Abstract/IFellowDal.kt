package com.beratyesbek.vhoops.DataAccess.Abstract

import com.beratyesbek.vhoops.Core.DataAccess.Abstract.IFirebaseFellowDal
import com.beratyesbek.vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFellowDal : IEntityRepository<Fellow>,IFirebaseFellowDal<Fellow> {
}