package android.chi.jason.cleanarchitecturedemo.ui

import android.chi.jason.cleanarchitecturedemo.R
import android.chi.jason.cleanarchitecturedemo.TaskTitleView
import android.chi.jason.cleanarchitecturedemo.db.TaskEntity
import android.graphics.Paint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_task.view.*

class TaskAdapter(private var taskList: MutableList<TaskEntity> = mutableListOf()
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(task: TaskEntity)
        fun onItemToggled(active: Boolean, task: TaskEntity)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun updateData(taskList: MutableList<TaskEntity>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(this.taskList, taskList))
        this.taskList = taskList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = run {
        TaskViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.setupView(taskList[position])
    }

    override fun getItemCount() = taskList.size

    private fun completionToggled(holder: TaskViewHolder) {
        onItemClickListener?.apply {
            onItemToggled(holder.itemView.checkbox.isChecked, taskList[holder.adapterPosition])
            if (holder.itemView.checkbox.isChecked) {
                holder.itemView.text_description.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.itemView.text_description.paintFlags = 0
            }
        }
    }

    private fun postItemClick(holder: TaskViewHolder) {
        onItemClickListener?.onItemClick(taskList[holder.adapterPosition])
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun setupView(task: TaskEntity) = itemView.apply {
            task.apply {
                text_description.text = description
                if (isComplete == 1) {
                    text_description.setState(TaskTitleView.DONE)
                    checkbox.isChecked = true
                } else {
                    text_description.setState(TaskTitleView.NORMAL)
                    checkbox.isChecked = false
                }
                if (hasDueDate()) {
                    text_date.text = DateUtils.getRelativeTimeSpanString(dueDateMills)
                    text_date.visibility = View.VISIBLE
                    if (task.dueDateMills < System.currentTimeMillis()) {
                        text_description.setState(TaskTitleView.OVERDUE)
                    }
                } else {
                    text_date.visibility = View.GONE
                }
                priority.setImageResource(if (isPriority == 1) R.drawable.ic_priority else R.drawable.ic_not_priority)
            }
        }

        override fun onClick(v: View) {
            if (v === itemView.checkbox) {
                completionToggled(this)
            } else {
                postItemClick(this)
            }
        }
    }

    class TaskDiffCallback(private val oldList: MutableList<TaskEntity>,
                           private val newList: MutableList<TaskEntity>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = run {
            oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = run {
            oldList[oldItemPosition].id == newList[newItemPosition].id
        }
    }
}