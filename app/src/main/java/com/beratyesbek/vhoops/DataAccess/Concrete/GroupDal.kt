package com.beratyesbek.vhoops.DataAccess.Concrete

import com.beratyesbek.vhoops.Core.DataAccess.Concrete.FirebaseGroupDal
import com.beratyesbek.vhoops.DataAccess.Abstract.IGroupDal

class GroupDal : IGroupDal, FirebaseGroupDal() {
}