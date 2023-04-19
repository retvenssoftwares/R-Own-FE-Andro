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

fun saveUserId(context:Context,user_id : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveUserId",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("user_id", user_id)
    editor.apply()
}