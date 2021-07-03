package com.beratyesbek.vhoops.dataAccess.concretes

import com.beratyesbek.vhoops.core.dataAccess.concretes.FirebaseGroupDal
import com.beratyesbek.vhoops.dataAccess.abstracts.IGroupDal

class GroupDal : IGroupDal, FirebaseGroupDal() {
}