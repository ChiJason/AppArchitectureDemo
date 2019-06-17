package android.chi.jason.cleanarchitecturedemo.ui


import android.app.DatePickerDialog
import android.chi.jason.cleanarchitecturedemo.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.chi.jason.cleanarchitecturedemo.R
import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.chi.jason.cleanarchitecturedemo.di.Injectable
import android.text.format.DateUtils
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*
import javax.inject.Inject

class AddTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(context as MainActivity, viewModelFactory).get(TaskViewModel::class.java)
    }

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        select_date.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(context, this, year, month, day).apply {
                datePicker.minDate = System.currentTimeMillis()
            }.show()
        }

        viewModel.dueDate.observe(this, Observer {
            if (it == Long.MAX_VALUE) {
                text_date.setText(R.string.date_empty)
            } else {
                val formatted = DateUtils.getRelativeTimeSpanString(context, it)
                text_date.text = formatted
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add_task, menu)
        menu?.findItem(R.id.action_settings)?.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //noinspection SimplifiableIfStatement
        if (item?.itemId == R.id.action_save) {
            //save item
            if (text_input_description.text.toString().trim().isEmpty()) {
                Toast.makeText(context, "description can't be blank", Toast.LENGTH_SHORT).show()
                return true
            }
            viewModel.addTask(TaskEntity(description = text_input_description.text.toString().trim(),
                    isComplete = 0,
                    isPriority = if (switch_priority.isChecked) 1 else 0,
                    dueDateMills = viewModel.dueDate.value ?: Long.MAX_VALUE))
            (context as MainActivity).setFragment(TaskFragment.newInstance())
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        //Set to noon on the selected day
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        c.set(Calendar.HOUR_OF_DAY, 12)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)

        viewModel.dueDate.value = c.timeInMillis
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dueDate.value = Long.MAX_VALUE
    }
}
