package app.retvens.rown.Dashboard.createPosts

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.R
import app.retvens.rown.databinding.ActivityCreatePollBinding
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePollActivity : AppCompatActivity() {
    lateinit var binding:ActivityCreatePollBinding


    lateinit var progressDialog: Dialog

    var pollNumber = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createCommunityBackBtn.setOnClickListener { onBackPressed() }

        binding.sharePoll.setOnClickListener {
            val sharedPreferences =  getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
            val user_id = sharedPreferences?.getString("user_id", "").toString()

            if (binding.etAddStatement.text!!.isEmpty()){
                binding.addStatement.error = "Add Statement"
            }else if (binding.opinion1.text!!.isEmpty()){
                binding.addStatement.isErrorEnabled = false
                binding.opinion1Layout.error = "Add Opinion"
            }else if (binding.opinion2.text!!.isEmpty()){
                binding.addStatement.isErrorEnabled = false
                binding.opinion1Layout.isErrorEnabled = false
                binding.opinion2Layout.error = "Add Opinion"
            }else if (binding.opinion3.text!!.isEmpty() && (pollNumber == 3 || pollNumber == 4 || pollNumber == 5) ){
                binding.addStatement.isErrorEnabled = false
                binding.opinion1Layout.isErrorEnabled = false
                binding.opinion2Layout.isErrorEnabled = false
                binding.opinion3Layout.error = "Add Opinion"
            }else if (binding.opinion4.text!!.isEmpty() && (pollNumber == 4 || pollNumber == 5) ){
                binding.addStatement.isErrorEnabled = false
                binding.opinion1Layout.isErrorEnabled = false
                binding.opinion2Layout.isErrorEnabled = false
                binding.opinion3Layout.isErrorEnabled = false
                binding.opinion4Layout.error = "Add Opinion"
            }else if (binding.opinion5.text!!.isEmpty() && (pollNumber == 5) ){
                binding.addStatement.isErrorEnabled = false
                binding.opinion1Layout.isErrorEnabled = false
                binding.opinion2Layout.isErrorEnabled = false
                binding.opinion3Layout.isErrorEnabled = false
                binding.opinion4Layout.isErrorEnabled = false
                binding.opinion5Layout.error = "Add Opinion"
            }else{
                progressDialog = Dialog(this)
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog.setCancelable(false)
                progressDialog.setContentView(R.layout.progress_dialoge)
                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val image = progressDialog.findViewById<ImageView>(R.id.imageview)
                Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
                progressDialog.show()
                createPoll(user_id)
            }


        }

        binding.sharePoll.isClickable = false

        binding.opinion1.addTextChangedListener {
            if (binding.opinion1.text!!.isNotEmpty()) {
                binding.sharePoll.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_own))
                binding.sharePoll.isClickable = true
            }
        }
        binding.opinion2.addTextChangedListener {
            if (binding.opinion2.text!!.isNotEmpty()) {
                binding.sharePoll.setCardBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.green_own
                    )
                )
                binding.sharePoll.isClickable = true
            }
        }

        binding.opinion3Layout.setEndIconOnClickListener {
            if (pollNumber == 5) {
                pollNumber = 4
                binding.opinion5Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 4) {
                pollNumber = 3
                binding.opinion4Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 3) {
                pollNumber = 2
                binding.opinion3Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            }
        }

        binding.opinion4Layout.setEndIconOnClickListener {
            if (pollNumber == 5) {
                pollNumber = 4
                binding.opinion5Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 4) {
                pollNumber = 3
                binding.opinion4Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 3) {
                pollNumber = 2
                binding.opinion3Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            }
        }

        binding.opinion5Layout.setEndIconOnClickListener {
            if (pollNumber == 5) {
                pollNumber = 4
                binding.opinion5Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 4) {
                pollNumber = 3
                binding.opinion4Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            } else if (pollNumber == 3) {
                pollNumber = 2
                binding.opinion3Layout.visibility = View.GONE
                binding.nextUpdateEvent.visibility = View.VISIBLE
            }
        }

        binding.nextUpdateEvent.setOnClickListener {
            if (pollNumber < 5) {
                pollNumber++
            }

            if (pollNumber == 3){
                binding.opinion3Layout.visibility = View.VISIBLE
            }else if (pollNumber == 4){
                binding.opinion4Layout.visibility = View.VISIBLE
            } else if (pollNumber == 5){
                binding.opinion5Layout.visibility = View.VISIBLE
                binding.nextUpdateEvent.visibility = View.GONE
            }
        }


    }

    private fun createPoll(userId: String) {

        val statement = binding.etAddStatement.text.toString()
        val opinion1 = binding.opinion1.text.toString()
        val opinion2 = binding.opinion2.text.toString()
        val opinion3 = binding.opinion3.text.toString()
        val opinion4 = binding.opinion4.text.toString()
        val opinion5 = binding.opinion5.text.toString()

        Log.e("option1",opinion1)
        Log.e("option2",opinion2)
        Log.e("option3",opinion3)
        Log.e("option4",opinion4)
        Log.e("option5",opinion5)

        if (pollNumber == 2) {
            val sendPoll = RetrofitBuilder.feedsApi.createPoll(
                userId,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Polls"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), statement),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion2)
            )

            sendPoll.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        Log.e("check1","1")
                        val response = response.body()!!
                        val intent = Intent(applicationContext, DashBoardActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            })
        } else if (pollNumber == 3) {
            val sendPoll = RetrofitBuilder.feedsApi.create3Poll(
                userId,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Polls"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), statement),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion2),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion3)
            )

            sendPoll.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        Log.e("check1","2")
                        val response = response.body()!!
                        val intent = Intent(applicationContext, DashBoardActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            })
        }  else if (pollNumber == 4) {
            val sendPoll = RetrofitBuilder.feedsApi.create4Poll(
                userId,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Polls"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), statement),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion2),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion3),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion4)
            )

            sendPoll.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        Log.e("check1","3")
                        val response = response.body()!!
                        val intent = Intent(applicationContext, DashBoardActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            })
        }  else if (pollNumber == 5) {
            val sendPoll = RetrofitBuilder.feedsApi.create5Poll(
                userId,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Polls"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), statement),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion2),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion3),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion4),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), opinion5)
            )

            sendPoll.enqueue(object : Callback<UpdateResponse?> {
                override fun onResponse(
                    call: Call<UpdateResponse?>,
                    response: Response<UpdateResponse?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        Log.e("check1","4")
                        val response = response.body()!!
                        val intent = Intent(applicationContext, DashBoardActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    } else {
//                    Toast.makeText(applicationContext,response.code().toString(),Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                    progressDialog.dismiss()
//                Toast.makeText(applicationContext,t.message.toString(),Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            })
        }
    }
}