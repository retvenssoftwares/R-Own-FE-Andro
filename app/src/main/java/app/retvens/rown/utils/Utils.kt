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

fun clearUserId(context: Context){
    val settings = context.getSharedPreferences("SaveUserId", Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.clear()
    editor.apply()
}

fun saveProgress(context:Context,progress : String){
    val editor : SharedPreferences.Editor = context.getSharedPreferences("SaveProgress",
        AppCompatActivity.MODE_PRIVATE
    ).edit()
    editor.putString("progress", progress)
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