package app.retvens.rown.NavigationFragments.profile.setting.profileSetting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.onboarding.ContactDetail
import app.retvens.rown.DataCollections.onboarding.ContactResponse
import app.retvens.rown.DataCollections.onboarding.ContactsData
import app.retvens.rown.R
import app.retvens.rown.authentication.UserContacts
import app.retvens.rown.databinding.ActivitySyncContactsBinding
import app.retvens.rown.utils.moveTo
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncContactsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySyncContactsBinding
    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
    lateinit var progressDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyncContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reBackBtn.setOnClickListener { onBackPressed() }

        progressDialog = Dialog(this)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setCancelable(false)
        progressDialog.setContentView(R.layout.progress_dialoge)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image = progressDialog.findViewById<ImageView>(R.id.imageview)
        Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
        progressDialog.show()
        checkContacts()

        binding.goContacts.setOnClickListener {
            progressDialog = Dialog(this)
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setCancelable(false)
            progressDialog.setContentView(R.layout.progress_dialoge)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val image = progressDialog.findViewById<ImageView>(R.id.imageview)
            Glide.with(applicationContext).load(R.drawable.animated_logo_transparent).into(image)
            progressDialog.show()

            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    loadContacts()
                }
            }

        }
    }

    private fun checkContacts() {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        val upload = RetrofitBuilder.retrofitBuilder.checkContacts(CheckContacts(user_id))
        upload.enqueue(object : Callback<ContactResponse?> {
            override fun onResponse(
                call: Call<ContactResponse?>,
                response: Response<ContactResponse?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful){
                    if (response.body()!!.message == "Synced") {
                        binding.svgUploaded.visibility = View.VISIBLE
                        binding.layout.visibility = View.GONE
                    }
                    Toast.makeText(applicationContext, "${response.body()!!.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ContactResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun loadContacts() {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //callback onRequestPermissionsResult
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                UserContacts.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            val myT = Thread {
                getContacts()
            }
            myT.start()
//            builder = getContacts()
//            binding.text2.text = builder.toString()
//            Log.d("Name ===>", builder.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == UserContacts.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    //callback onRequestPermissionsResult
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        UserContacts.PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                } else {
                    try {
                        loadContacts()
                    }catch (e:Exception){
                        Toast.makeText(applicationContext,e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                progressDialog.dismiss()
                Toast.makeText(this,"Permission must be granted in order to display contacts information",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val listOfContacts = mutableListOf<ContactDetail>()
//        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val rawContactId = getRawContactId(id)
                val companyName = getCompanyName(rawContactId!!)
                val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
//                var phoneNumValue
                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if(cursorPhone!!.count > 0) {
                        var phoneNumber : String = ""
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.let {
                                cursorPhone.getString(
                                    it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            }
//                            builder.append("Contact: ").append(name)
//                                .append(",\nCompany: ").append(companyName)
//                                .append(",\nPhone Number: ").append(phoneNumValue).append("\n\n")
                            phoneNumber = phoneNumValue
//                            Log.d("Name ===>", companyName.toString())
//                            companyName?.let { Log.d("Name ===>", it) }
                        }
                        listOfContacts.add(ContactDetail(companyName.toString(),name.toString(),phoneNumber.toString()))
                    }
                    cursorPhone.close()
                }
            }
        } else {
            Toast.makeText(applicationContext,"No more contacts", Toast.LENGTH_SHORT).show()
        }
//        listOfContacts.forEach {
//            Log.d("cont", "${it.Name} ${it.Number} ${it.Company_Name}\n")
//        }
        cursor.close()

        uploadContacts(listOfContacts)
    }
    @SuppressLint("Range")
    private fun getRawContactId(id: String?): String? {
        val projection = arrayOf(ContactsContract.RawContacts._ID)
        val selection = ContactsContract.RawContacts.CONTACT_ID + "=?"
        val selectionArgs = arrayOf(id)
        val c: Cursor = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
            ?: return null
        var rawContactId = -1
        if (c.moveToFirst()) {
            rawContactId = c.getInt(c.getColumnIndex(ContactsContract.RawContacts._ID))
        }
        c.close()
        return rawContactId.toString()
    }
    @SuppressLint("Range")
    private fun getCompanyName(rawContactId: String): String? {
        return try {
            val orgWhere =
                ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
            val orgWhereParams = arrayOf(
                rawContactId,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
            )
            val cursor: Cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                null, orgWhere, orgWhereParams, null
            ) ?: return null
            var name: String? = null
            if (cursor.moveToFirst()) {
                name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
            }
            cursor.close()
            name
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun uploadContacts(listOfContacts: MutableList<ContactDetail>) {
        val sharedPreferences = getSharedPreferences("SaveUserId", AppCompatActivity.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "").toString()
        val upload = RetrofitBuilder.retrofitBuilder.uploadContacts(ContactsData(listOfContacts,user_id))

        listOfContacts.forEach {
            Log.d("con","${it.Name}, ${it.Number}, ${it.Company_Name} ")
        }
        upload.enqueue(object : Callback<ContactResponse?> {
            override fun onResponse(
                call: Call<ContactResponse?>,
                response: Response<ContactResponse?>
            ) {
//                Toast.makeText(applicationContext,response.body()?.message.toString(),Toast.LENGTH_SHORT).show()
                Log.d("cont",response.body().toString())
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    moveTo(this@SyncContactsActivity,"MoveToD")
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext,"${response.code().toString()} - Retry (Connection Lost)",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ContactResponse?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("cont", "ContactsApi : ${ t.localizedMessage?.toString() }",t)
//                Toast.makeText(applicationContext,"Error - ${t.localizedMessage}",Toast.LENGTH_SHORT).show()
            }
        })
    }
}