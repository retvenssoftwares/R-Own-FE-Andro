package app.retvens.rown.Dashboard.profileCompletion.frags

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.retvens.rown.ApiRequest.RetrofitBuilder
import app.retvens.rown.Dashboard.DashBoardActivity
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateResponse
import app.retvens.rown.DataCollections.ProfileCompletion.UpdateUserName
import app.retvens.rown.R
import app.retvens.rown.utils.saveUserId
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mesibo.calls.api.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UsernameFragment : Fragment() {

    lateinit var dobEt : TextInputEditText
    private lateinit var firstName:TextInputEditText
    private lateinit var lastName:TextInputEditText
    private lateinit var userName:TextInputEditText
    private lateinit var sdf:SimpleDateFormat
    private lateinit var cal:Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_username, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*-------------Calendar Setup--------------*/
        dobEt = view.findViewById(R.id.dob_et)

        cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            sdf = SimpleDateFormat(myFormat, Locale.US)
            dobEt.setText(sdf.format(cal.time))


        }
        view.findViewById<ImageView>(R.id.dob_cal_img).setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        /*-------------Calendar Setup--------------*/


        //Define Fields
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
         userName = view.findViewById(R.id.complete_username)

        val complete = view.findViewById<CardView>(R.id.card_complete_continue)

        complete.setOnClickListener {
            sendInfo()
        }
    }

    private fun sendInfo() {

        val first = firstName.text.toString()
        val last = lastName.text.toString()
        val username = userName.text.toString()

        val fullName = "$first $last"

        val date = sdf.format(cal.time).toString()

        val update = UpdateUserName(fullName,date,username)

        val send = RetrofitBuilder.profileCompletion.setUsername("Oo7PCzo0-",update)

        send.enqueue(object : Callback<UpdateResponse?> {
            override fun onResponse(
                call: Call<UpdateResponse?>,
                response: Response<UpdateResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()

                    val fragment = LocationFragment()
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_username,fragment)
                    transaction?.commit()
                }else{
                    Toast.makeText(requireContext(),response.code().toString(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateResponse?>, t: Throwable) {
                Toast.makeText(requireContext(),t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
            }
        })







    }
}
