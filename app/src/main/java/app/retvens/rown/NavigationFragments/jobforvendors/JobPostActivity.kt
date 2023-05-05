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
import androidx.fragment.app.FragmentActivity
import app.retvens.rown.R
import app.retvens.rown.bottomsheet.BottomSheetCTC
import app.retvens.rown.bottomsheet.BottomSheetJobType
import app.retvens.rown.bottomsheet.BottomSheetLocation
import com.google.android.material.textfield.TextInputEditText

class JobPostActivity : AppCompatActivity(),
    BottomSheetJobType.OnBottomJobTypeClickListener,
    BottomSheetCTC.OnBottomCTCClickListener, BottomSheetLocation.OnBottomLocationClickListener {

    lateinit var jobTypeEt : TextInputEditText
    lateinit var postMinSalaryEt : TextInputEditText
    lateinit var postMaxSalaryEt : TextInputEditText
    lateinit var postLocationEt : TextInputEditText

    var etType : Int ? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_post)

        jobTypeEt = findViewById(R.id.et_post_job_type)
        postMinSalaryEt = findViewById(R.id.et_post_min_salary)
        postMaxSalaryEt = findViewById(R.id.et_post_max_salary)
        postLocationEt = findViewById(R.id.et_location_job_post)
        postLocationEt.setOnClickListener {
            val bottomSheet = BottomSheetLocation()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetLocation.LOCATION_TAG)}
            bottomSheet.setOnLocationClickListener(this)
        }

        jobTypeEt.setOnClickListener {
            val bottomSheet = BottomSheetJobType()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetJobType.Job_TYPE_TAG)}
            bottomSheet.setOnJobTypeClickListener(this)
        }
        postMinSalaryEt.setOnClickListener {
            etType = 1
            val bottomSheet = BottomSheetCTC()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }
        postMaxSalaryEt.setOnClickListener {
            etType = 2
            val bottomSheet = BottomSheetCTC()
            val fragManager = supportFragmentManager
            fragManager.let{bottomSheet.show(it, BottomSheetCTC.CTC_TAG)}
            bottomSheet.setOnCTCClickListener(this)
        }
    }
    override fun bottomJobTypeClick(jobTypeFrBo: String) {
        jobTypeEt.setText(jobTypeFrBo)
    }

    override fun bottomCTCClick(CTCFrBo: String) {
        if (etType == 1){
            postMinSalaryEt.setText(CTCFrBo)
        }else{
            postMaxSalaryEt.setText(CTCFrBo)
        }
    }

    override fun bottomLocationClick(LocationFrBo: String) {

    }
}