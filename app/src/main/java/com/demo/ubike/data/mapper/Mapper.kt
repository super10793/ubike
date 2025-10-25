package com.demo.ubike.data.mapper

abstract class Mapper<From, To> {

    abstract fun map(from: From): To
}