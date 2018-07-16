package com.djonus.recyclerclick

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

fun RecyclerView.clicks(@IdRes viewId: Int, listener: (ItemClick) -> Unit) {
    val onClickListener = View.OnClickListener {
        val click = ItemClick(it, it.positionInRecycler())
        listener(click)
    }

    setClickListeners(viewId, onClickListener)
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView?.setClickListeners(viewId, onClickListener)
            }
        }
    })
}

private fun RecyclerView.setClickListeners(@IdRes viewId: Int, listener: View.OnClickListener) {
    for (i in 0 until childCount) {
        getChildAt(i).withId(viewId)?.apply {
            setOnClickListener(listener)
        }
    }
}

private fun View.positionInRecycler(): Int = if (parent is RecyclerView) {
    (parent as RecyclerView).getChildAdapterPosition(this)
} else {
    (parent as View).positionInRecycler()
}

private fun View.withId(@IdRes viewId: Int): View? = if (id == viewId) this else findViewById(viewId)

