package com.beratyesbek.vhoops.core.dataAccess.concretes

import android.net.Uri
import com.beratyesbek.vhoops.core.dataAccess.abstracts.IFirebaseUserDal
import com.beratyesbek.vhoops.core.dataAccess.constants.FirebaseCollection
import com.beratyesbek.vhoops.core.dataAccess.IEntityRepository
import com.beratyesbek.vhoops.core.EmailVerification
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IDataResult
import com.beratyesbek.vhoops.core.utilities.results.Abstract.IResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.ErrorDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.ErrorResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessDataResult
import com.beratyesbek.vhoops.core.utilities.results.Concrete.SuccessResult
import com.beratyesbek.vhoops.entities.concrete.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList


open class FirebaseUserDal : IEntityRepository<User>, IFirebaseUserDal<User> {

    private lateinit var cloudFirebase: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage

    override fun add(entity: User, result: (IResult) -> Unit) {

        cloudFirebase = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        val hashmap = hashMapOf(
            "FirstName" to entity.firstName,
            "LastName" to entity.lastName,
            "Email" to entity.email,
            "UserName" to entity.userName,
            "About" to "Hi there, I am using Vhoops"

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

        cloudFirebase = FirebaseFirestore.getInstance()

        val hashMap = hashMapOf(
            "FirstName" to entity.firstName,
            "LastName" to entity.lastName,
            "Token" to entity.token,
            "About" to entity.about,
            "UserID" to entity.userID
        )

        val documentID = entity.documentID
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .document(documentID)
            .update(hashMap as Map<String, Any>)
            .addOnSuccessListener {
                result(SuccessResult("updated"))
            }.addOnFailureListener {
                result(SuccessResult("failed"))

            }

    }

    override fun delete(entity: User, result: (IResult) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAll(iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        val userList = ArrayList<User>()
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .get()
            .addOnSuccessListener { task ->
                userList.addAll(setUserData(task))
                iDataResult(SuccessDataResult<ArrayList<User>>(userList, "listed"))
            }.addOnFailureListener {

            }

    }

    override fun createUser(entity: User, result: (IResult) -> Unit) {

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth
            .createUserWithEmailAndPassword(entity.email!!, entity.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //  verificationEmail(entity.email!!,result)
                    result(SuccessResult("user was created"))

                } else {
                    result(ErrorResult("user wasn't created"))

                }
            }

    }

    override fun loginUser(email: String, password: String, result: (IResult) -> Unit) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if(!it.user.isEmailVerified){
                EmailVerification.sendEmail()
                result(ErrorResult("You didn't verification your email. A verification email was sent to your email. Please verify your email"))
                firebaseAuth.signOut()
            }
            else{
                result(SuccessResult(""))

            }
        }.addOnFailureListener {
            result(ErrorResult("Your email or password is wrong"))
        }
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
                    userList.addAll(setUserData(documents))
                    iDataResult(SuccessDataResult<ArrayList<User>>(userList, ""))

                } else {
                    iDataResult(ErrorDataResult<ArrayList<User>>(userList, ""))
                }
            }
    }

    override fun uploadPhoto(uri: Uri, result: (IResult) -> Unit) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance()
        val userId = firebaseAuth.currentUser.uid
        val path = "UserProfileImage/" + userId + "/profileImage/"
        firebaseStorage.reference.child(path)
            .putFile(uri)
            .addOnSuccessListener {
                result(SuccessResult("uploaded"))

            }.addOnFailureListener {
                result(SuccessResult("failed"))
            }
    }

    override fun getPhoto(userId: String, result: (IDataResult<Uri>) -> Unit) {
        firebaseStorage = FirebaseStorage.getInstance()
        val path = "UserProfileImage/" + userId + "/profileImage/"
        firebaseStorage.reference.child(path)
            .downloadUrl
            .addOnSuccessListener { uri ->
                result(SuccessDataResult(uri, ""))
            }.addOnFailureListener {
                result(ErrorDataResult(null, "Failed"))
            }
    }

    override fun updateUserName(userName: String, documentId: String, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance();

        val hashMap = hashMapOf(
            "UserName" to userName
        )
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .document(documentId)
            .update(hashMap as Map<String, Any>)
            .addOnSuccessListener {
                result(SuccessResult("updated"))
            }.addOnFailureListener {
                result(SuccessResult("failed"))
            }
    }

    override fun getById(id: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        val userList = ArrayList<User>()
        cloudFirebase = FirebaseFirestore.getInstance()
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .whereEqualTo("UserID", id).addSnapshotListener { value, error ->
                if (value != null) {
                    userList.addAll(setUserData(value))
                    iDataResult(SuccessDataResult<ArrayList<User>>(userList, ""))

                } else {
                    iDataResult(ErrorDataResult<ArrayList<User>>(userList, ""))
                }
            }
    }

    override fun getByEmail(email: String, iDataResult: (IDataResult<ArrayList<User>>) -> Unit) {
        val userList = ArrayList<User>()
        cloudFirebase = FirebaseFirestore.getInstance();
        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {
                    userList.addAll(setUserData(documents))
                    iDataResult(SuccessDataResult<ArrayList<User>>(userList, ""))

                } else {
                    iDataResult(ErrorDataResult<ArrayList<User>>(userList, ""))
                }
            }
    }


    /*---------------------------SET DATA-------------------------------------*/
    // this method of property isn't repeat herself
    // if don't write this method , must write in getById,getByName,getAll


    private fun setUserData(value: QuerySnapshot): ArrayList<User> {
        val userList = ArrayList<User>()
        for (document in value) {
            val documentID = document.id
            val firstName = document.get("FirstName").toString()
            val lastName = document.get("LastName").toString()
            val userName = document.get("UserName").toString()
            val userID = document.get("UserID").toString()
            val token = document.get("Token").toString()
            val email = document.get("Email").toString()
            val profileImage = document.get("ProfileImage")
            val about = document.get("About").toString()
            var uriImage: Uri? = null
            if (profileImage != null) {
                uriImage = Uri.parse(profileImage.toString());
            }

            userList.add(
                User(
                    firstName,
                    lastName,
                    email,
                    userName,
                    "",
                    userID,
                    token,
                    uriImage,
                    documentID,
                    about
                )
            )
        }
        return userList;

    }

    override fun updateUserProfileImage(uri: Uri?, documentId: String, result: (IResult) -> Unit) {
        cloudFirebase = FirebaseFirestore.getInstance()
        var imageUri: String? = null

        if (uri != null) {
            imageUri = uri.toString()
        }
        val hashMap = hashMapOf(
            "ProfileImage" to imageUri
        )


        cloudFirebase.collection(FirebaseCollection.USER_COLLECTION)
            .document(documentId)
            .update(hashMap as Map<String, Any>)
            .addOnSuccessListener {
                result(SuccessResult("updated"))
            }.addOnFailureListener {
                result(SuccessResult("failed"))

            }

    }

    override fun verificationEmail(email: String, result: (IResult) -> Unit) {

        val user = FirebaseAuth.getInstance().currentUser
        user.sendEmailVerification().addOnSuccessListener {

        }.addOnFailureListener {

        }

    }

    override fun removeProfileImage(result: (IResult) -> Unit) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance()

        val userId = firebaseAuth.currentUser.uid
        val path = "UserProfileImage/" + userId

        firebaseStorage.reference.child(path)
            .delete()
            .addOnSuccessListener {
                result(SuccessResult(""));

            }.addOnFailureListener {
                result(ErrorResult(""));
            }
    }


}



