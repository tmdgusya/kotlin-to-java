@file:JvmName("Shortlists")

package chapter08

import java.util.stream.Collectors

fun <T> sorted(
    sortedlist: List<T>,
    ordering: Comparator<in T>?
): List<T> {
    return sortedlist
        .stream()
        .sorted(ordering)
        .collect(Collectors.toUnmodifiableList())
}