package com.github.jnexil.neuvi.util

import com.github.jnexil.neuvi.api.providers.*
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.should.*
import org.jetbrains.spek.api.*
import org.jetbrains.spek.api.dsl.*
import java.util.concurrent.*

class CyclicKtTest: Spek(spec = {
    given("double vector") {
        on("replace") {
            it("should contain replaced values") {
                val vector = MemoryProvider.vector(5)
                vector.replace { 5.0 + it }
                vector.toArray().toList() shouldMatch equalTo(listOf(5.0, 6.0, 7.0, 8.0, 9.0))
            }
        }
        on("sum values") {
            val array = DoubleArray(5) { ThreadLocalRandom.current().nextInt(-5, 10).toDouble() }
            val vector = MemoryProvider.vector(array)
            it("should return sum of values") {
                vector.sumIndexed { i, value -> value } == array.sum()
            }
            it("should return sum of values using mapping") {
                vector.sumIndexed { i, value -> value * 2 } == array.sum() * 2
            }
            it("should sequentially iterate indices and values") {
                var index = 0
                vector.sumIndexed { i, value ->
                    assert(index++ == i) { "Invalid index" }
                    assert(array[i] == value) { "Invalid value" }
                    0.0
                }
            }
        }
    }
    given("double array") {
        on("sum values") {
            val array = DoubleArray(5) { ThreadLocalRandom.current().nextInt(-5, 10).toDouble() }
            it("should return sum of values") {
                array.sumIndexed { i, value -> value } == array.sum()
            }
            it("should return sum of values using mapping") {
                array.sumIndexed { i, value -> value * 2 } == array.sum() * 2
            }
            it("should sequentially iterate indices and values") {
                var index = 0
                array.sumIndexed { i, value ->
                    assert(index++ == i) { "Invalid index" }
                    assert(array[i] == value) { "Invalid value" }
                    0.0
                }
            }
        }
    }
    class NestedData(val index: Int, val nested: NestedData? = null)
    given("nested data with multiple depth") {
        val data = NestedData(1,
                              NestedData(2,
                                         NestedData(3)))
        on("find last by seed") {
            it("should return far") {
                Cyclic.findLast(seed = data, next = NestedData::nested).index shouldMatch equalTo(3)
            }
        }
    }
    given("nested data with none nested data") {
        val data = NestedData(1)
        on("find last by seed") {
            it("should return given element") {
                Cyclic.findLast(seed = data, next = NestedData::nested).index shouldMatch equalTo(1)
            }
        }
    }
    given("iterator with few data") {
        val iterator = arrayOf(1, 4, 2).iterator()
        on("find last without seed") {
            it("should return last element") {
                Cyclic.findLast {
                    iterator.nextOrNull()
                } shouldMatch equalTo(2)
            }
        }
    }
    given("iterator with one element") {
        val iterator = arrayOf(1).iterator()
        on("find last without seed") {
            it("should return given element") {
                Cyclic.findLast {
                    iterator.nextOrNull()
                } shouldMatch equalTo(1)
            }
        }
    }
    given("empty iterator") {
        val iterator = arrayOf<Any>().iterator()
        on("find last without seed") {
            it("should return null") {
                Cyclic.findLast {
                    iterator.nextOrNull()
                } shouldMatch equalTo(null as Any?)
            }
        }
    }
})