package com.beratyesbek.vhoops.dataAccess.abstracts

import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseFellowDal
import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.entities.concrete.Fellow

interface IFellowDal : IEntityRepository<Fellow>,IFirebaseFellowDal<Fellow> {
}