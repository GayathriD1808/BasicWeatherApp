package com.gayathri.basicweatherapp.common

interface Mapper<From, To> {
    fun mapFrom(from: From): To
}