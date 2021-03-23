package com.beratyesbek.Vhoops.Core.DataAccess.Concrete

import android.net.Uri
import com.beratyesbek.Vhoops.Core.Constants.Messages
import com.beratyesbek.Vhoops.Core.DataAccess.Abstract.IFirebaseUserDal
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.FirebaseCollection
import com.beratyesbek.Vhoops.Core.DataAccess.IEntityRepository
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Abstract.IResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.ErrorResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessDataResult
import com.beratyesbek.Vhoops.Core.Utilities.Result.Concrete.SuccessResult
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.android.awaitFrame


open class FirebaseUserDal : IEntityRepository<User>, IFirebaseUserDal<User> {

    private lateinit var cloudFirebase: FirebaseFirestore;
    private lateinit var firebaseAuth: FirebaseAuth;

    override fun add(entity: User, result: (IResult) -> Unit) {

        cloudFirebase = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        val hashmap = hashMapOf(
            "FirstName" to entity.firstName,
            "LastName" to entity.lastName,
            "Email" to entity.email,
            "UserName" to entity.userName,
        )
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .add(hashmap)
            .addOnSuccessListener {
                result(SuccessResult("user created"));


            }.addOnFailureListener {
                result(ErrorResult("user didn't create"))

            }
    }

    override fun update(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        val userList = ArrayList<User>()
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (item in result) {
                    val firstName = item.get("FirstName").toString()
                    val lastName = item.get("LastName").toString()
                    val email = item.get("Email").toString()
                    val userName = item.get("UserName").toString()

                    userList.add(User(firstName, lastName, email, userName, ""))

                }
                iDataResult(SuccessDataResult<ArrayList<User>>(userList,Messages.GET_DATA_SUCCESS))
            }.addOnFailureListener {

            }

    }

    override fun createUser(entity: User, result: (IResult) -> Unit) {

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth
            .createUserWithEmailAndPassword(entity.email!!, entity.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result(SuccessResult("user created by createUser method"))
                } else {
                    result(ErrorResult("user didn't create by createUser method"))

                }
            }

    }

    override fun loginUser(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getByUserName(
        userName: String,
        iDataResult: (IDataResult<ArrayList<User>>) -> Unit
    ) {
        val userList = ArrayList<User>()
        cloudFirebase = FirebaseFirestore.getInstance();
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .whereEqualTo("UserName", userName)
            .get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {
                    for (document in documents) {

                        val firstName = document.get("FirstName").toString()
                        val lastName = document.get("LastName").toString()
                        val _userName = document.get("UserName").toString()
                        val userID = document.get("UserID").toString()
                        val token = document.get("Token").toString()
                        val email = document.get("Email").toString()
                        val profileImage = document.get("ProfileImage")
                        var uriImage: Uri? = null
                        if (profileImage != null) {
                            uriImage = Uri.parse(profileImage.toString());
                        }
                        userList.add(
                            User(
                                firstName,
                                lastName,
                                email,
                                _userName,
                                "",
                                userID,
                                token,
                                uriImage
                            )
                        )
                        iDataResult(SuccessDataResult<ArrayList<User>>(userList, ""))
                    }
                } else {
                    iDataResult(ErrorDataResult<ArrayList<User>>(userList, ""))
                }
            }
    }

}



