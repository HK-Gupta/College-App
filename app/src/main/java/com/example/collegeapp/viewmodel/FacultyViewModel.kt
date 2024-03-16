package com.example.collegeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.models.FacultyModel
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.utils.Constants.BANNER
import com.example.collegeapp.utils.Constants.FACULTY
import com.example.collegeapp.utils.Constants.NOTICE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID

class FacultyViewModel: ViewModel() {

    private val facultyRef = Firebase.firestore.collection(FACULTY)
    private val storageRef = Firebase.storage.reference

    private val isPostedMutable = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = isPostedMutable

    private val isDeletedMutable = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = isDeletedMutable

    private val categoryListMutable = MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>> = categoryListMutable

    private val facultyListMutable = MutableLiveData<List<FacultyModel>>()
    val facultyList: LiveData<List<FacultyModel>> = facultyListMutable


    fun saveFaculty(uri: Uri, name: String, email: String, position: String) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$FACULTY/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                uploadFaculty(it.toString(), randomUid, name, email, position)
            }
        }

    }

    private fun uploadFaculty(imageUrl: String, docId: String, name: String,
                              email: String, position: String) {
        val map = mutableMapOf<String, String>()
        map["imageUrl"] = imageUrl
        map["docId"] = docId
        map["name"] = name
        map["email"] = email
        map["position"] = position

        facultyRef.document(docId).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getFaculty() {
        facultyRef.get().addOnSuccessListener {snapshot->
            val list = mutableListOf<FacultyModel>()

            for(doc in snapshot) {
                list.add(doc.toObject(FacultyModel::class.java))
            }

            facultyListMutable.postValue(list)
        }
    }

    fun deleteFaculty (facultyModel: FacultyModel) {

        facultyRef.document(facultyModel.docId!!).delete()
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(facultyModel.imageUrl!!).delete()
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.d("DeleteError", "Error deleting banner: ${exception.message}", exception)
            }
    }


}