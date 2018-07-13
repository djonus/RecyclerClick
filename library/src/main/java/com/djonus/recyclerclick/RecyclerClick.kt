package com.djonus.recyclerclick

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single

private const val TAG_EMITTER = 132645284642.toInt()

private val clickListener = View.OnClickListener { v ->
    val click = ItemClick(v, v.positionInRecycler())
    v.getEmitters().forEach {
        if (!it.isDisposed) it.onNext(click)
    }
}

fun RecyclerView.clicks(@IdRes viewId: Int): Observable<ItemClick> {

    lateinit var childAttachedListener: RecyclerView.OnChildAttachStateChangeListener

    return Observable.create<ItemClick> { emitter ->

        childAttachedListener = ChildAttachedListener(viewId) {
            it.addEmitter(emitter)
            it.setOnClickListener(clickListener)
        }

        for (i in 0 until childCount) {
            getChildAt(i).withId(viewId)?.apply {
                addEmitter(emitter)
                setOnClickListener(clickListener)
            }
        }

        this.addOnChildAttachStateChangeListener(childAttachedListener)
    }
            .takeUntil(this.onDetach().toObservable())
            .doOnTerminate {
                this.removeOnChildAttachStateChangeListener(childAttachedListener)
            }
}

fun View.positionInRecycler(): Int = if (parent is RecyclerView) {
    (parent as RecyclerView).getChildAdapterPosition(this)
} else {
    (parent as View).positionInRecycler()
}

private fun View.onDetach(): Single<Any> = Single.create<Any> {
    this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewDetachedFromWindow(v: View?) {
            this@onDetach.removeOnAttachStateChangeListener(this)
            it.onSuccess(Any())
        }

        override fun onViewAttachedToWindow(v: View) {
            //Nothing to do
        }
    })
}

private fun View.withId(@IdRes viewId: Int): View? = if (id == viewId) this else findViewById(viewId)

private fun View.addEmitter(emitter: ObservableEmitter<ItemClick>) {
    setTag(TAG_EMITTER, getEmitters().plus(emitter))
}

private fun View.getEmitters() = getTag(TAG_EMITTER) as? Set<ObservableEmitter<ItemClick>> ?: emptySet()

private class ChildAttachedListener(@IdRes val viewId: Int, val callback: (View) -> Any) : RecyclerView.OnChildAttachStateChangeListener {

    override fun onChildViewAttachedToWindow(view: View) {
        view.withId(viewId)?.let { callback(it) }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        //Nothing to do
    }
}

