package app.retvens.rown.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

fun moveTo(context:Context,state : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("Move",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("MoveTo", state)
    editor.apply()
}

fun moveToClear(context: Context){
    val sharedPreferences = context.getSharedPreferences("Move", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}

fun saveUserId(context:Context,user_id : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveUserId",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("user_id", user_id)
    editor.apply()
}

fun saveFullName(context:Context,fullName : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveFullName",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("full_name", fullName)
    editor.apply()
}

fun saveConnectionNo(context:Context,connectionNo : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveConnectionNo",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("connectionNo", connectionNo)
    editor.apply()
}

fun saveProfileImage(context:Context,profile : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveProfileImage",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("profile_image", profile)
    editor.apply()
}

fun saveProgress(context:Context,progress : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveProgress",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("progress", progress)
    editor.apply()
}
fun clearUserId(context: Context){
    val settings = context.getSharedPreferences("SaveUserId", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}
fun clearFullName(context: Context){
    val settings = context.getSharedPreferences("SaveFullName", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}

fun clearProfileImage(context: Context){
    val settings = context.getSharedPreferences("SaveProfileImage", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}

fun clearProgress(context: Context){
    val settings = context.getSharedPreferences("SaveProgress", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}
fun clearUserType(context: Context){
    val settings = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}