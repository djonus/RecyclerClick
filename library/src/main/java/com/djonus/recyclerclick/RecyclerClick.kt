package com.djonus.recyclerclick

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

private const val TAG_EMITTER = 132645284642.toInt()

private val defaultClickListener = View.OnClickListener {
    val click = ItemClick(it, it.positionInRecycler())
    it.getClickListeners().forEach { it(click) }
}

fun RecyclerView.clicks(@IdRes viewId: Int, listener: (ItemClick) -> Unit) {
    setClickListeners(viewId, listener)
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView?.setClickListeners(viewId, listener)
            }
        }
    })
}

private fun RecyclerView.setClickListeners(@IdRes viewId: Int, listener: (ItemClick) -> Unit) {
    for (i in 0 until childCount) {
        getChildAt(i).withId(viewId)?.apply {
            addClickListener(listener)
            setOnClickListener(defaultClickListener)
        }
    }
}

private fun View.positionInRecycler(): Int = if (parent is RecyclerView) {
    (parent as RecyclerView).getChildAdapterPosition(this)
} else {
    (parent as View).positionInRecycler()
}

private fun View.addClickListener(emitter: (ItemClick) -> Unit) {
    setTag(TAG_EMITTER, getClickListeners().plus(emitter))
}

private fun View.getClickListeners() = getTag(TAG_EMITTER) as? Set<(ItemClick) ->Unit> ?: emptySet()

private fun View.withId(@IdRes viewId: Int): View? = if (id == viewId) this else findViewById(viewId)

