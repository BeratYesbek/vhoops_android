package com.beratyesbek.Vhoops.DataAccess.Abstract

import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseFellowDal
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Entities.Concrete.Fellow

interface IFellowDal : IEntityRepository<Fellow>,IFirebaseFellowDal<Fellow> {
}