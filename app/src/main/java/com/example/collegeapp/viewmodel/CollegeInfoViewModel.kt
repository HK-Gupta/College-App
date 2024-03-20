package com.example.collegeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.models.CollegeInfoModel
import com.example.collegeapp.utils.Constants.BANNER
import com.example.collegeapp.utils.Constants.COLLEGE_INFO
import com.example.collegeapp.utils.Constants.DETAILED_COLLEGE_INFO
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID

class CollegeInfoViewModel: ViewModel() {

    private val collegeInfoRef = Firebase.firestore.collection(COLLEGE_INFO)
    private val storageRef = Firebase.storage.reference

    private val isPostedMutable = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = isPostedMutable

    private val collegeInfoMutable = MutableLiveData<CollegeInfoModel>()
    val collegeInfo : LiveData<CollegeInfoModel> = collegeInfoMutable


    fun saveCollegeInfo(uri: Uri, name: String, address: String,
                  desc: String, websiteLink: String) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$COLLEGE_INFO/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                uploadCollegeInfo(it.toString(), name, address, desc, websiteLink)
            }
        }

    }

    fun uploadCollegeInfo(imageUrl: String, name: String,
                            address: String, desc: String, websiteLink: String) {
        val map = mutableMapOf<String, Any>()
        map["imageUrl"] = imageUrl
        map["name"] = name
        map["address"] = address
        map["desc"] = desc
        map["websiteLink"] = websiteLink

        // After setting up the data make it update
        collegeInfoRef.document(DETAILED_COLLEGE_INFO).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getCollegeInfo() {
        collegeInfoRef.document(DETAILED_COLLEGE_INFO).get().addOnSuccessListener { it->
            collegeInfoMutable.postValue(
                CollegeInfoModel(
                    it.data!!["name"].toString(),
                    it.data!!["address"].toString(),
                    it.data!!["desc"].toString(),
                    it.data!!["websiteLink"].toString(),
                    it.data!!["imageUrl"].toString(),
                )
            )

        }
    }

}