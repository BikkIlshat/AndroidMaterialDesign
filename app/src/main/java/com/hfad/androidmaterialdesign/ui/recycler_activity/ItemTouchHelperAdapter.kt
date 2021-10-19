package com.hfad.androidmaterialdesign.ui.recycler_activity

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}