package com.arjun.compose_mvvm_retrofit

import android.content.Context
import app.retvens.rown.DataCollections.MesiboResponseClass


class SharedPreferenceManagerAdmin private constructor(private val context : Context){

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("__v", -1) != -1
        }

    val user: MesiboResponseClass
        get() {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return MesiboResponseClass(
                sharedPreferences.getString("access_token",null),
                sharedPreferences.getString("message", null),
//                sharedPreferences.getBoolean("success", false),
                sharedPreferences.getString("token_type", null),
                sharedPreferences.getInt("__v",-1)
            )
        }

    fun saveUser(user: MesiboResponseClass) {

        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("access_token", user.address)
        editor.putString("message", user.token)
//      editor.putBoolean("success", user.success)
        editor.putString("token_type", user.uid)
        editor.putInt("__v", user.__v)
        editor.apply()
    }

    fun clear() {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPreferenceManagerAdmin? = null
        @Synchronized
        fun getInstance(context: Context): SharedPreferenceManagerAdmin {
            if (mInstance == null) {
                mInstance = SharedPreferenceManagerAdmin(context)
            }
            return mInstance as SharedPreferenceManagerAdmin
        }
    }
 }