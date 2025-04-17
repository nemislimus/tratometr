package com.nemislimus.tratometr.expenses.ui.fragment.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemislimus.tratometr.expenses.ui.fragment.adpter.ExpensesAdapter

class ExpensesRecyclerView(
    val context: Context,
    val recycler: RecyclerView,
    val adapter: ExpensesAdapter
) {

    fun setupRecyclerView() {
        recycler.adapter = adapter
        recycler.setLayoutManager(object : LinearLayoutManager(context) {
            // Отменяем скольжение
            override fun requestChildRectangleOnScreen(
                parent: RecyclerView,
                child: View,
                rect: Rect,
                immediate: Boolean,
                focusedChildVisible: Boolean
            ): Boolean = false
        })
        recycler.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 150
        }
        recycler.scheduleLayoutAnimation()
        recycler.invalidate()
    }
}