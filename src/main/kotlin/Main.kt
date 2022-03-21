import kotlin.math.max

fun main(args: Array<String>) {

    println("Merge Intervals #1 --------------------------------------")
    var listOfPairs = listOf(Pair(1,4), Pair(2,5), Pair(7,9))
    println("[[1,4], [2,5], [7,9]]")
    println("Expected: [[1,5], [7,9]] , actual: ${mergeIntervals(listOfPairs)}")
    listOfPairs = listOf(Pair(6,7), Pair(2,4), Pair(5,9))
    println("[[6,7], [2,4], [5,9]]")
    println("Expected: [[2,4], [5,9]] , actual: ${mergeIntervals(listOfPairs)}")
    listOfPairs = listOf(Pair(1,4), Pair(2,6), Pair(3,5))
    println("[[1,4], [2,6], [3,5]]")
    println("Expected: [[1,6]] , actual: ${mergeIntervals(listOfPairs)}")

    listOfPairs = listOf(Pair(1,4), Pair(2,5), Pair(7,9))
    println("Merge Intervals #1.2 --------------------------------------")
    println("[[1,4], [2,5], [7,9]]")
    println("Expected: true , actual: ${doesOverlap(listOfPairs)}")
    listOfPairs = listOf(Pair(1,2), Pair(7,9), Pair(3,5))
    println("Merge Intervals #1.2")
    println("[[1,2], [3,5], [7,9]]")
    println("Expected: false , actual: ${doesOverlap(listOfPairs)}")

    println("Merge Intervals #2.1 --------------------------------------")
    listOfPairs = listOf(Pair(1,3), Pair(5,7), Pair(8,12))
    println("Merge Intervals #1.2")
    println("[[1,3], [5,7], [8,12]], New Interval=[4,6]")
    println("Expected: [[1,3], [4,7], [8,12]] , actual: ${addInterval(listOfPairs, Pair(4,6))}")
    listOfPairs = listOf(Pair(1,3), Pair(5,7), Pair(8,12))
    println("[[1,3], [5,7], [8,12]], New Interval=[4,10]")
    println("Expected: [[1,3], [4,12]] , actual: ${addInterval(listOfPairs, Pair(4,10))}")
    listOfPairs = listOf(Pair(2,3), Pair(5,7))
    println("[[2,3],[5,7]], New Interval=[1,4]")
    println("Expected: [[1,4], [5,7]] , actual: ${addInterval(listOfPairs, Pair(1,4))}")
}

/*
    addInterval takes in a list of Pairs and a new Pair to add
        Each pair represents the start and end of an interval
        All intervals given are assumed non-overlapping
        Function adds the new Pair and merges where required to maintain mutual exclusivity
 */

/*
    mergeIntervals takes in a list of Pairs.
       Each pair represents the start and end of an interval
       The function merges all the overlapping intervals to produce a list of mutually exclusive intervals
 */
fun mergeIntervals(intervals: List<Pair<Int,Int>>): MutableList<Pair<Int,Int>> {
    if(intervals.size < 2) return intervals.toMutableList()
    val resultInterval = mutableListOf<Pair<Int,Int>>()
    // sort the list by starting position of each interval
    val sortedIntervals = intervals.sortedBy { it.first }
    // then we can start comparing the ending position of each interval to see if we need to merge
    // loop from start to the penultimate interval
    var index = 1
    val a = sortedIntervals[0]
    var cFirst = a.first
    var cSecond = a.second
    while(index <= sortedIntervals.size-1) {
        val b = sortedIntervals[index]
        if(b.first <= cSecond) {
            cSecond = max(b.second, cSecond)
        } else {
            resultInterval.add(Pair(cFirst, cSecond))
            cFirst = b.first
            cSecond = b.second
        }
        index++
    }
    resultInterval.add(Pair(cFirst,cSecond))
    return resultInterval
}

fun doesOverlap(intervals: List<Pair<Int,Int>>): Boolean {
    if(intervals.size < 2) return false
    // first sort the list
    val sortedList = intervals.sortedBy{ it.first }
    var a = sortedList.first()
    var index = 1
    while(index < sortedList.size) {
        var b = sortedList[index]
        if(b.first <= a.second) return true
        else {
            a = b
        }
        index++
    }
    return false
}
