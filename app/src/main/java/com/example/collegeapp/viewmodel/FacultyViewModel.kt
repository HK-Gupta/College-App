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
import com.example.collegeapp.utils.Constants.CATEGORY
import com.example.collegeapp.utils.Constants.FACULTY
import com.example.collegeapp.utils.Constants.NOTICE
import com.example.collegeapp.utils.Constants.TEACHER
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


    fun saveFaculty(uri: Uri, name: String, email: String, position: String, category: String) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$FACULTY/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                uploadFaculty(it.toString(), randomUid, name, email, position, category)
            }
        }

    }

    private fun uploadFaculty(imageUrl: String, docId: String, name: String,
                              email: String, position: String, category: String) {
        val map = mutableMapOf<String, String>()
        map["imageUrl"] = imageUrl
        map["docId"] = docId
        map["name"] = name
        map["email"] = email
        map["position"] = position
        map[CATEGORY] = category

        facultyRef.document(category).collection(TEACHER).document(docId).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getFaculty(catName: String) {
        facultyRef.document(catName).collection(TEACHER)
            .get().addOnSuccessListener { snapshot->
            val list = mutableListOf<FacultyModel>()

            for(doc in snapshot) {
                list.add(doc.toObject(FacultyModel::class.java))
            }

            facultyListMutable.postValue(list)
        }
    }

    fun deleteFaculty (facultyModel: FacultyModel) {

        facultyRef.document(facultyModel.category_name!!).collection(TEACHER)
            .document(facultyModel.docId!!).delete().addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(facultyModel.imageUrl!!).delete()
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.d("DeleteError", "Error deleting Faculty: ${exception.message}", exception)
            }
    }


    // Category
    fun uploadCategory (category: String) {
        val map = mutableMapOf<String, String>()
        map[CATEGORY] = category

        facultyRef.document(category).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }
    fun getCategory() {
        facultyRef.get().addOnSuccessListener {snapshot->
            val list = mutableListOf<String>()

            for(doc in snapshot) {
                list.add(doc.get(CATEGORY).toString())
            }

            categoryListMutable.postValue(list)
        }
    }
    fun deleteCategory (category: String) {

        facultyRef.document(category).delete()
            .addOnSuccessListener {
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.d("DeleteError", "Error deleting Category: ${exception.message}", exception)
            }
    }

}