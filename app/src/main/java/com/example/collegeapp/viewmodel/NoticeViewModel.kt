package com.example.collegeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeapp.models.BannerModel
import com.example.collegeapp.models.NoticeModel
import com.example.collegeapp.utils.Constants.BANNER
import com.example.collegeapp.utils.Constants.NOTICE
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import java.util.UUID

class NoticeViewModel: ViewModel() {

    private val noticeRef = Firebase.firestore.collection(NOTICE)
    private val storageRef = Firebase.storage.reference

    private val isPostedMutable = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = isPostedMutable

    private val isDeletedMutable = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = isDeletedMutable

    private val noticeListMutable = MutableLiveData<List<NoticeModel>>()
    val noticeList: LiveData<List<NoticeModel>> = noticeListMutable




    fun saveNotice(uri: Uri, title: String, link: String) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$NOTICE/${randomUid}.jpg")

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                uploadNotice(it.toString(), randomUid, title, link)
            }
        }

    }

    private fun uploadNotice(imageUrl: String, docId: String, title: String, link: String) {
        val map = mutableMapOf<String, String>()
        map["imageUrl"] = imageUrl
        map["docId"] = docId
        map["title"] = title
        map["link"] = link

        noticeRef.document(docId).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getNotice() {
        noticeRef.get().addOnSuccessListener {snapshot->
            val list = mutableListOf<NoticeModel>()

            for(doc in snapshot) {
                list.add(doc.toObject(NoticeModel::class.java))
            }

            noticeListMutable.postValue(list)
        }
    }

    fun deleteNotice (noticeModel: NoticeModel) {

        noticeRef.document(noticeModel.docId!!).delete()
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(noticeModel.imageUrl!!).delete()
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.d("DeleteError", "Error deleting notice: ${exception.message}", exception)
            }
    }


}