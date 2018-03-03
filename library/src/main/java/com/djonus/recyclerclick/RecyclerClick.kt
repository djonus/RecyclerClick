package com.djonus.recyclerclick

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single

fun RecyclerView.clicks(@IdRes viewId: Int): Observable<ItemClick> {
    lateinit var clicksToEmitter: RecyclerView.OnChildAttachStateChangeListener

    return Observable.create<ItemClick> {
        clicksToEmitter = ChildClicksToEmitterAttachListener(viewId, it)
        this.addOnChildAttachStateChangeListener(clicksToEmitter)
    }
            .takeUntil(this.onDetach().toObservable())
            .doOnTerminate({
                this.removeOnChildAttachStateChangeListener(clicksToEmitter)
            })
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

private class ChildClicksToEmitterAttachListener(@IdRes val viewId: Int, val emitter: ObservableEmitter<ItemClick>) : RecyclerView.OnChildAttachStateChangeListener {

    private val clickListener = View.OnClickListener { v ->
        if (v != null && !emitter.isDisposed) {
            val click = ItemClick(v, v.positionInRecycler())
            v.getEmitters().forEach {
                it.onNext(click)
            }
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        if (view.id == viewId) {
            view.addEmitter(emitter)
            view.setOnClickListener(clickListener)
        } else {
            view.findViewById<View>(viewId)?.apply {
                addEmitter(emitter)
                setOnClickListener(clickListener)
            }
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        view.setOnClickListener(null)
    }

    private fun View.addEmitter(emitter: ObservableEmitter<ItemClick>) {
        setTag(TAG_EMITTER, getEmitters().plus(emitter))
    }

    private fun View.getEmitters() = getTag(TAG_EMITTER) as? Set<ObservableEmitter<ItemClick>> ?: emptySet()

    companion object {
        const val TAG_EMITTER = 132645284642.toInt()
    }
}

