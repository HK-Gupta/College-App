package com.example.collegeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.utils.Constants.BANNER
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID

class BannerViewModel: ViewModel() {

    private val bannerRef = Firebase.firestore.collection(BANNER)
    private val storageRef = Firebase.storage.reference

    private val isPostedMutable = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = isPostedMutable

    private val isDeletedMutable = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = isDeletedMutable

    private val bannerListMutable = MutableLiveData<List<BannerModel>>()
    val bannerList: LiveData<List<BannerModel>> = bannerListMutable




    fun saveImage(uri: Uri) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$BANNER/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnCompleteListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                uploadImage(it.toString(), randomUid)
            }
        }

    }

    private fun uploadImage(imageUrl: String, docId: String) {
        val map = mutableMapOf<String, String>()
        map["url"] = imageUrl
        map["docId"] = docId

        bannerRef.document().set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getBanner() {
        bannerRef.get().addOnSuccessListener {snapshot->
            val list = mutableListOf<BannerModel>()

            for(doc in snapshot) {
                list.add(doc.toObject(BannerModel::class.java))
            }

            bannerListMutable.postValue(list)
        }
    }

    fun deleteBanner(bannerModel: BannerModel) {

        bannerRef.document(bannerModel.docId!!).delete()
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(bannerModel.url!!).delete()
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.e("DeleteError", "Error deleting banner: ${exception.message}", exception)

            }
    }

}