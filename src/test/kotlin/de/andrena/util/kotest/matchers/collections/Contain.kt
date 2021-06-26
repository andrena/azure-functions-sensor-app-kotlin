package de.andrena.util.kotest.matchers.collections

import io.kotest.assertions.show.show
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

fun <T, C : Collection<T>> C.shouldContain(t: T, comparator: T.(T) -> Boolean) {
    this should contain(t, comparator)
}

fun <T, C : Collection<T>> contain(t: T, comparator: T.(T) -> Boolean) = object : Matcher<C> {

    override fun test(value: C) = MatcherResult(
        value.any { comparator(it, t) },
        { "Collection should contain element ${t.show().value}; listing some elements ${value.take(5)}" },
        { "Collection should not contain element ${t.show().value}" }
    )

}
