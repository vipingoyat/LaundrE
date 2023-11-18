package com.azhar.laundry.daos

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    @SuppressLint("RestrictedApi")
    fun adduser(user: com.azhar.laundry.models.User) {
        user?.let{
            GlobalScope.launch(Dispatchers.IO) {
                user.uid?.let { it1 -> usersCollection.document(it1).set(it) }

            }
        }
    }
}