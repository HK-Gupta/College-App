package com.example.collegeapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.collegeapp.models.GalleryModel
import com.example.collegeapp.utils.Constants.GALLERY
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class GalleryViewModel: ViewModel() {

    private val galleryRef = Firebase.firestore.collection(GALLERY)
    private val storageRef = Firebase.storage.reference

    private val isPostedMutable = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = isPostedMutable

    private val isDeletedMutable = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> = isDeletedMutable

    private val galleryListMutable = MutableLiveData<List<GalleryModel>>()
    val galleryList: LiveData<List<GalleryModel>> = galleryListMutable


    fun saveGalleryImage(uri: Uri, category: String, isCategory: Boolean) {
        isPostedMutable.postValue(false)

        val randomUid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$GALLERY/${randomUid}.jpg")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {task->
            imageRef.downloadUrl.addOnSuccessListener { it->
                if(isCategory)
                    uploadCategory(it.toString(), category)
                else
                    updateImage(it.toString(), category)
            }
        }
    }

    private fun uploadCategory (image: String, category: String) {
        val map = mutableMapOf<String, Any>()
        map["category"] = category
        map["images"] = FieldValue.arrayUnion(image)  // To store array list in firestore

        galleryRef.document(category).set(map)
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    private fun updateImage (image: String, category: String) {

        galleryRef.document(category).update("images", FieldValue.arrayUnion(image))
            .addOnSuccessListener {
                isPostedMutable.postValue(true)
            }.addOnFailureListener {
                isPostedMutable.postValue(false)
            }
    }

    fun getGallery() {
        galleryRef.get().addOnSuccessListener { snapshot->
            val list = mutableListOf<GalleryModel>()

            for(doc in snapshot) {
                list.add(doc.toObject(GalleryModel::class.java))
            }

            galleryListMutable.postValue(list)
        }
    }

    fun deleteGallery (galleryModel: GalleryModel) {

        galleryModel.images!!.forEach { it->
            Firebase.storage.getReferenceFromUrl(it).delete()
        }

        galleryRef.document(galleryModel.category!!).delete()
            .addOnSuccessListener {
                isDeletedMutable.postValue(true)
            }
            .addOnFailureListener {exception->
                isDeletedMutable.postValue(false)
                Log.d("DeleteError", "Error deleting Faculty: ${exception.message}", exception)
            }
    }

    fun deleteImage(category: String, image: String) {
        galleryRef.document(category).update("images", FieldValue.arrayRemove(image))
            .addOnSuccessListener {
                Firebase.storage.getReferenceFromUrl(image).delete()
                isDeletedMutable.postValue(true)
            }.addOnFailureListener {
                isDeletedMutable.postValue(false)
            }
    }



}