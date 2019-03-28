package com.mobgen.presentation

interface ViewHolder<T> {
    fun render(value: T)
}