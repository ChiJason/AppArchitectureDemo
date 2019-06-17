package android.chi.jason.cleanarchitecturedemo.ui


import android.chi.jason.cleanarchitecturedemo.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.chi.jason.cleanarchitecturedemo.R
import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.chi.jason.cleanarchitecturedemo.di.Injectable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_task.*
import javax.inject.Inject

class TaskFragment : Fragment(), TaskAdapter.OnItemClickListener, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(context as MainActivity, viewModelFactory).get(TaskViewModel::class.java)
    }

    private val listAdapter by lazy {
        TaskAdapter().apply {
            setOnItemClickListener(this@TaskFragment)
        }
    }

    companion object {
        fun newInstance() = TaskFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addTaskFab.setOnClickListener {
            (context as MainActivity).setFragment(AddTaskFragment.newInstance())
        }
        taskRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        viewModel.taskList.observe(this, Observer {
            listAdapter.updateData(it)
        })
        if (PreferenceManager.getDefaultSharedPreferences(context)
                .getString(resources.getString(R.string.pref_sortBy_key),
                    resources.getString(R.string.pref_sortBy_default)) == resources.getString(R.string.pref_sortBy_due)) {
            viewModel.loadTasksSortByDueDate()
        } else {
            viewModel.loadTasks()
        }
    }

    override fun onItemClick(task: TaskEntity) {
        viewModel.selectedTask.value = task
        (context as MainActivity).setFragment(TaskDetailFragment.newInstance())
    }

    override fun onItemToggled(active: Boolean, task: TaskEntity) {
        viewModel.updateTaskCompletion(active, task.id)
    }
}
