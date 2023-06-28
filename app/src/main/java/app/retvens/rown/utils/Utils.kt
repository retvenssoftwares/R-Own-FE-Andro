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
fun savePhoneNo(context:Context,phone : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("savePhoneNo",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("savePhoneNumber", phone)
    editor.apply()
}

fun saveConnectionNo(context:Context,connectionNo : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveConnectionNo",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("connectionNo", connectionNo)
    editor.apply()
}
fun clearConnectionNo(context: Context){
    val settings = context.getSharedPreferences("SaveConnectionNo", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}
fun clearPhone(context: Context){
    val settings = context.getSharedPreferences("savePhoneNo", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}

fun saveProfileImage(context:Context,profile : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveProfileImage",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("profile_image", profile)
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