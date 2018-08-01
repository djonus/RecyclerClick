package com.djonus.recyclerclick

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

fun RecyclerView.clicks(@IdRes viewId: Int, listener: (ItemClick) -> Unit) {
    val onClickListener = View.OnClickListener {
        val click = ItemClick(it, it.positionInRecycler())
        listener(click)
    }

    addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            //Nothing to do
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view.withId(viewId)?.apply {
                setOnClickListener(onClickListener)
            }
        }
    })
}

private fun View.positionInRecycler(): Int = if (parent is RecyclerView) {
    (parent as RecyclerView).getChildAdapterPosition(this)
} else {
    (parent as View).positionInRecycler()
}

private fun View.withId(@IdRes viewId: Int): View? = if (id == viewId) this else findViewById(viewId)

