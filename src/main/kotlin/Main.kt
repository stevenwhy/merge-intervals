import kotlin.math.max
import kotlin.math.min

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

    var arr1 = listOf(Interval(1,3), Interval(5,6), Interval(7,9))
    var arr2 = listOf(Interval(2,3), Interval(5,7))
    println("Merge Intervals #3.1 --------------------------------------")
    println("arr1=[[1, 3], [5, 6], [7, 9]], arr2=[[2, 3], [5, 7]]")
    println("Expected: [2, 3], [5, 6], [7, 7] , actual: ${findCommonIntervals(arr1, arr2)}")
    arr1 = listOf(Interval(1,3), Interval(5,7), Interval(9,12))
    arr2 = listOf(Interval(5,7), Interval(9,10))
    println("arr1=[[1, 3], [5, 7], [9, 12]], arr2=[[5, 10]]")
    println("Expected: [5, 7], [9, 10] , actual: ${findCommonIntervals(arr1, arr2)}")
}

/*
    findCommonIntervals takes in 2 lists of intervals, each sorted by interval start
        returns list of intervals which overlap both input lists
 */
fun findCommonIntervals(arr1: List<Interval>, arr2: List<Interval>): MutableList<Interval> {
    val result = mutableListOf<Interval>()
    var index1 = 0
    var index2 = 0

    while(index1 < arr1.size && index2 < arr2.size) {
        var a = arr1[index1] // 1,3
        var b = arr2[index2] // 5,10
        var newInterval = Interval(0,0)
        if(b.second < a.first) {
            //no overlap b before a
            index2++
            continue
        } else if (a.second < b.first) {
            //no overlap a before b
            index1++
            continue
        } else {
            //overlap
            newInterval.first = max(a.first, b.first)
            newInterval.second = min(a.second, b.second)
            println("adding $newInterval")
            result.add(newInterval)
        }
        if(a.second < b.second) index1++
        if(b.second < a.second) index2++
        if(a.second == b.second) {
            index1++
            index2++
        }

    }
    return result
}

/*
    addInterval takes in a list of Pairs and a new Pair to add
        Each pair represents the start and end of an interval
        All intervals given are assumed non-overlapping, sorted by start value
        Function adds the new Pair and merges where required to maintain mutual exclusivity
 */
fun addInterval(intervals: MutableList<Interval>, newInterval: Interval): MutableList<Interval> {

    var index = 0
    while(index < intervals.size) {
        val temp = intervals[index]
        if(temp.second < newInterval.first) index++
        else {
            // there is an overlap
            val start = min(temp.first, newInterval.first)
            //now we need to find the end value
            var end = max(temp.second, newInterval.second)
            // but this can overlap with next value
            val indexToUpdate = index
            index++
            while(index < intervals.size) {
                if(end < intervals[index].first) break
                else if (end <= intervals[index].second) {
                    end = intervals[index].second
                    intervals.removeAt(index)
                    break
                }
                intervals.removeAt(index)
            }
            intervals[indexToUpdate].first = start
            intervals[indexToUpdate].second = end
            break
        }
    }
    return intervals
}
data class Interval(var first: Int, var second: Int)
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
