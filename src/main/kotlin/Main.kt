import kotlin.math.max
import kotlin.math.min

data class Interval(var first: Int, var second: Int) {
    override fun toString(): String {
        return "[$first, $second]"
    }
}

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


    println("Merge Intervals #1.2 --------------------------------------")
    listOfPairs = listOf(Pair(1,4), Pair(2,5), Pair(7,9))
    println("[[1,4], [2,5], [7,9]]")
    println("Expected: true , actual: ${doesOverlap(listOfPairs)}")
    listOfPairs = listOf(Pair(1,2), Pair(7,9), Pair(3,5))
    println("[[1,2], [3,5], [7,9]]")
    println("Expected: false , actual: ${doesOverlap(listOfPairs)}")

    println("Merge Intervals #2.1 --------------------------------------")
    var listOfIntervals = listOf(Interval(1,3), Interval(5,7), Interval(8,12))
    println("[[1,3], [5,7], [8,12]], New Interval=[4,6]")
    println("Expected: [[1,3], [4,7], [8,12]] , actual: ${addInterval(listOfIntervals.toMutableList(), Interval(4,6))}")
    listOfIntervals = listOf(Interval(1,3), Interval(5,7), Interval(8,12))
    println("[[1,3], [5,7], [8,12]], New Interval=[4,10]")
    println("Expected: [[1,3], [4,12]] , actual: ${addInterval(listOfIntervals.toMutableList(), Interval(4,10))}")
    listOfIntervals = listOf(Interval(2,3), Interval(5,7))
    println("[[2,3],[5,7]], New Interval=[1,4]")
    println("Expected: [[1,4], [5,7]] , actual: ${addInterval(listOfIntervals.toMutableList(), Interval(1,4))}")

    var arr1 = listOf(Interval(1,3), Interval(5,6), Interval(7,9))
    var arr2 = listOf(Interval(2,3), Interval(5,7))
    println("Merge Intervals #3.1 --------------------------------------")
    println("arr1=[[1, 3], [5, 6], [7, 9]], arr2=[[2, 3], [5, 7]]")
    println("Expected: [2, 3], [5, 6], [7, 7] , actual: ${findCommonIntervals(arr1, arr2)}")
    arr1 = listOf(Interval(1,3), Interval(5,7), Interval(9,12))
    arr2 = listOf(Interval(5,7), Interval(9,10))
    println("arr1=[[1, 3], [5, 7], [9, 12]], arr2=[[5, 10]]")
    println("Expected: [5, 7], [9, 10] , actual: ${findCommonIntervals(arr1, arr2)}")

    println("Merge Intervals #4.1 Conflicting Appts--------------------------------------")
    listOfIntervals = listOf(Interval(1,4), Interval(2,5), Interval(7,9))
    println("[[1,4], [2,5], [7,9]]")
    println("Expected: false , actual: ${canAttend(listOfIntervals)}")
    listOfIntervals = listOf(Interval(6,7), Interval(2,4), Interval(8,12))
    println("[[6,7], [2,4], [8,12]]")
    println("Expected: true , actual: ${canAttend(listOfIntervals)}")
    listOfIntervals = listOf(Interval(4,5), Interval(2,3), Interval(3,6))
    println("[[4,5], [2,3], [3,6]]")
    println("Expected: false , actual: ${canAttend(listOfIntervals)}")
}

/*
    canAttend takes in a list of unsorted intervals,
        returns a boolean of if any overlap
 */
fun canAttend(intervals: List<Interval>): Boolean {
    if(intervals.size < 2) return true

    val sortedList = intervals.sortedBy { it.first }
    var index = 1
    var a = sortedList.first() // 3,6
    while(index < sortedList.size) {
        var b = sortedList[index] // 4,5
        if(b.first < a.second) {
            // over lap
            return false
        }
        a = b
        index++
    }
    return true
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
