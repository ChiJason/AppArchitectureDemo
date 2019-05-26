package android.chi.jason.cleanarchitecturedemo

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class TaskTitleView : AppCompatTextView {

    companion object {
        const val NORMAL = 0
        const val DONE = 1
        const val OVERDUE = 2
    }

    private var state: Int? = null

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    /**
     * Return the current display state of this view.
     *
     * @return One of {@link #NORMAL}, {@link #DONE}, or {@link #OVERDUE}.
     */
    fun getState() = this.state

    /**
     * Update the text display state of this view.
     * Normal status shows black text. Overdue displays in red.
     * Completed draws a strikethrough line on the text.
     *
     * @param state New state. One of {@link #NORMAL}, {@link #DONE}, or {@link #OVERDUE}.
     */
    fun setState(state: Int) {
        when (state) {
            NORMAL -> {
                paintFlags = 0
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            DONE -> {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            OVERDUE -> {
                paintFlags = 0
                setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
        this.state = state
    }
}