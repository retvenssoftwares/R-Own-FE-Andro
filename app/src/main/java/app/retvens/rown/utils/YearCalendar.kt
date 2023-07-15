package app.retvens.rown.utils

import android.content.Context
import android.widget.EditText
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.Calendar

 fun endYearDialog(context: Context, editText: EditText, selectedYear : Int){

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val builder : MonthPickerDialog.Builder  = MonthPickerDialog.Builder(context,
        MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
                    editText.setText("$selectedYear")
        }, Calendar.YEAR, Calendar.MONTH)

    builder.setActivatedYear(selectedYear)
//                .setMaxYear(2030)
        .setTitle("Select Ending Year")
        .setYearRange(selectedYear, selectedYear+5)
        .showYearOnly()
        .setOnYearChangedListener {
            editText.setText("$it")
        }
        .build()
        .show()

}
