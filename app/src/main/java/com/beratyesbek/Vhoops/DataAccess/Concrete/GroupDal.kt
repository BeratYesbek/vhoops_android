package com.beratyesbek.Vhoops.DataAccess.Concrete

import com.beratyesbek.Vhoops.Core.DataAccess.Concrete.FirebaseGroupDal
import com.beratyesbek.Vhoops.DataAccess.Abstract.IGroupDal

class GroupDal : IGroupDal, FirebaseGroupDal() {
}