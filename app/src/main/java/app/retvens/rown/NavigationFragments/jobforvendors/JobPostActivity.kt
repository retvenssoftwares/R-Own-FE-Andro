package app.retvens.rown.NavigationFragments.jobforvendors

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import app.retvens.rown.R
import com.google.android.material.textfield.TextInputEditText

class JobPostActivity : AppCompatActivity() {

    lateinit var jobTypeEt : TextInputEditText
    lateinit var postMinSalaryEt : TextInputEditText
    lateinit var postMaxSalaryEt : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_post)

        jobTypeEt = findViewById(R.id.et_post_job_type)
        postMinSalaryEt = findViewById(R.id.et_post_min_salary)
        postMaxSalaryEt = findViewById(R.id.et_post_max_salary)

        jobTypeEt.setOnClickListener {
            showBottomJobType()
        }
        postMinSalaryEt.setOnClickListener {
            showBottomCTC(postMinSalaryEt)
        }
        postMaxSalaryEt.setOnClickListener {
            showBottomCTC(postMaxSalaryEt)
        }
    }

    private fun showBottomJobType() {

        val dialogRole = Dialog(this)
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_job_type)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            jobTypeEt.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fullTime).setOnClickListener {
            jobTypeEt.setText("Full-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.partTime).setOnClickListener {
            jobTypeEt.setText("Part-Time")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.selfE).setOnClickListener {
            jobTypeEt.setText("Self-Employed")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Freelancer).setOnClickListener {
            jobTypeEt.setText("Freelancer")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Internship).setOnClickListener {
            jobTypeEt.setText("Internship")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.Trainee).setOnClickListener {
            jobTypeEt.setText("Trainee")
            dialogRole.dismiss()
        }
    }

    private fun showBottomCTC(salaryEt: TextInputEditText) {

        val dialogRole = Dialog(this)
        dialogRole.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRole.setContentView(R.layout.bottom_sheet_expected_ctc)
        dialogRole.setCancelable(true)

        dialogRole.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogRole.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRole.window?.attributes?.windowAnimations = R.style.DailogAnimation
        dialogRole.window?.setGravity(Gravity.BOTTOM)
        dialogRole.show()

        dialogRole.findViewById<RelativeLayout>(R.id.seleOne).setOnClickListener {
            salaryEt.setText("Select one -")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.one_to_three).setOnClickListener {
            salaryEt.setText("1-3 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.three_to_six).setOnClickListener {
            salaryEt.setText("3-6 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.six_to_ten).setOnClickListener {
            salaryEt.setText("6-10 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.ten_to_fif).setOnClickListener {
            salaryEt.setText("10-15  Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.fif_to_tf).setOnClickListener {
            salaryEt.setText("15-25 Lakhs/p.a")
            dialogRole.dismiss()
        }
        dialogRole.findViewById<RelativeLayout>(R.id.more_than_tf).setOnClickListener {
            salaryEt.setText(">25 Lakhs/p.a")
            dialogRole.dismiss()
        }
    }

}