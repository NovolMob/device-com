package ru.novolmob.backendapi.transformations

interface Transformation<Value> {
    operator fun invoke(value: Value): Value
}



