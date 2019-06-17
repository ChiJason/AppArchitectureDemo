package android.chi.jason.cleanarchitecturedemo.ui

import android.chi.jason.cleanarchitecturedemo.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.chi.jason.cleanarchitecturedemo.R
import android.chi.jason.cleanarchitecturedemo.di.Injectable
import android.text.format.DateUtils
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_task_detail.*
import javax.inject.Inject

class TaskDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(context as MainActivity, viewModelFactory).get(TaskViewModel::class.java)
    }

    companion object {
        fun newInstance() = TaskDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.selectedTask.observe(this, Observer {
            detail_description.text = it.description
            detail_dueDate.text = if (it.hasDueDate()) {
                DateUtils.getRelativeTimeSpanString(it.dueDateMills)
            } else {
                resources.getString(R.string.date_empty)
            }
            dateil_priority.setImageResource(if (it.isPriority == 1) R.drawable.ic_priority else R.drawable.ic_not_priority)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_task_detail, menu)
        menu?.findItem(R.id.action_settings)?.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            viewModel.deleteTask(viewModel.selectedTask.value!!.id)
            (context as MainActivity).setFragment(TaskFragment.newInstance())
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
