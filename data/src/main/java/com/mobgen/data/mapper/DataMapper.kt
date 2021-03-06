package com.mobgen.data.mapper

import java.util.*

interface DataMapper<T1, T2> {

    fun map(value: T1): T2?

    fun map(values: List<T1>): List<T2> {
        val returnValues = ArrayList<T2>(values.size)
        for (value in values) {
            returnValues.add(map(value)!!)
        }
        return returnValues
    }
}