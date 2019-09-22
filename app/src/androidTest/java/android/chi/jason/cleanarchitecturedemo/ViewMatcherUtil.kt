package android.chi.jason.cleanarchitecturedemo

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object ViewMatcherUtil {

    fun recyclerViewSizeMatcher(expectedSize: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("with size: $expectedSize")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                return expectedSize == item?.adapter!!.itemCount
            }
        }
    }

    fun viewHolderMatcher(recyclerViewId: Int, position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description?) {
                val idDescription = resources?.getResourceName(recyclerViewId) ?: String.format(
                    "%s (resource name not found)",
                    recyclerViewId
                )
                description?.appendText("RecyclerView with id: $idDescription at position: $position")
            }

            override fun matchesSafely(item: View?): Boolean {
                resources = item?.resources

                if (childView == null) {
                    val recyclerView = item?.rootView?.findViewById<RecyclerView>(recyclerViewId)
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                        if (viewHolder != null) {
                            childView = viewHolder.itemView
                        }
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    item == childView
                } else {
                    val targetView = childView?.findViewById<View>(targetViewId)
                    item == targetView
                }
            }

        }
    }
}