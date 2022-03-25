import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Merge Intervals Problem 1 Minimum Meeting Rooms --------------------------------------")
    var listOfIntervals = listOf(Interval(1,4), Interval(2,5), Interval(7,9))
    println("[[1,4], [2,5], [7,9]]")
    println("Expected: 2 , actual: ${minRooms(listOfIntervals)}")
    listOfIntervals = listOf(Interval(6,7), Interval(2,4), Interval(8,12))
    println("[[6,7], [2,4], [8,12]]")
    println("Expected: 1 , actual: ${minRooms(listOfIntervals)}")
    listOfIntervals = listOf(Interval(4,5), Interval(2,3), Interval(2,4), Interval(3,5))
    println("[[4,5], [2,3], [2,4], [3,5]]")
    println("Expected: 2 , actual: ${minRooms(listOfIntervals)}")
    listOfIntervals = listOf(Interval(2,3), Interval(2,4), Interval(3,5), Interval(4,6))
    println("[[2,3], [2,4], [3,5], [4,6]]")
    println("Expected: 2 , actual: ${minRooms(listOfIntervals)}")
    listOfIntervals = listOf(Interval(2,3), Interval(3,4), Interval(3,5), Interval(4,6))
    println("[[2,3], [2,4], [3,5], [4,6]]")
    println("Expected: 2 , actual: ${minRooms(listOfIntervals)}")



    println("Merge Intervals Problem 2 Max CPU Load --------------------------------------")
    var listOfTriples = listOf(Triple(1,4,3), Triple(2,5,4), Triple(7,9,6))
    println("[[1,4,3], [2,5,4], [7,9,6]]")
    println("Expected: 7 , actual: ${maxCPU(listOfTriples)}")
    listOfTriples = listOf(Triple(6,7,10), Triple(2,4,11), Triple(8,12,15))
    println("[[6,7,10], [2,4,11], [8,12,15]]")
    println("Expected: 15 , actual: ${maxCPU(listOfTriples)}")
    listOfTriples = listOf(Triple(1,4,2), Triple(2,4,1), Triple(3,6,5))
    println("[[1,4,2], [2,4,1], [3,6,5]]")
    println("Expected: 8 , actual: ${maxCPU(listOfTriples)}")
}

/*
    Each job has a Start time, an End time, and a CPU load when it is running.
    Our goal is to find the maximum CPU load at any time if all the jobs are running on the same machine.
 */
fun maxCPU(intervals: List<Triple<Int,Int,Int>>): Int {
    if(intervals.isEmpty()) return 0
    var maxCPU = 0
    val minHeap = PriorityQueue<Triple<Int,Int,Int>>(compareBy { it.second })
    val sortedJobs = intervals.sortedBy { it.first }
    for(job in sortedJobs) {
        // clear heap of jobs that have ended
        while(minHeap.isNotEmpty() && job.first >= minHeap.peek().second) {
            minHeap.remove()
        }
        // add job to minHeap
        minHeap.add(job)
        var sum = 0
        for(overlap in minHeap) sum += overlap.third
        maxCPU = max(maxCPU, sum)
    }

    return maxCPU
}
/*
    Given a list of intervals representing the start and end time of ‘N’ meetings,
    find the minimum number of rooms required to hold all the meetings.
 */
fun minimumRooms(intervals: List<Interval>): Int {
    if(intervals.size  <= 1) return intervals.size

    val sortedList = intervals.sortedBy { it.first }
    var index = 1
    var result = 1
    var rooms = 1
    var a = sortedList.first() // 2 4
    var lowestEnd = a.second // 3
    while(index < sortedList.size) {
        val b = sortedList[index] // 3 5
        if(b.first < a.second && b.first < lowestEnd) {
            //overlap
            rooms++
            lowestEnd = min(lowestEnd, b.second)
        } else {
            // no overlap
            result = max(result, rooms)
            rooms = 1
            lowestEnd = b.second
        }
        a = b
        index++
    }
    return result
}
// better solution
fun minRooms(intervals: List<Interval>): Int {
    if(intervals.size <= 1) return intervals.size

    var minRooms = 0
    // min heap of end meeting times
    val minHeap = PriorityQueue<Interval>(compareBy { it.second })
    val sortedList = intervals.sortedBy { it.first }

    for(meeting in sortedList) {
        // remove meetings that have ended
        while(minHeap.isNotEmpty() && meeting.first >= minHeap.peek().second) {
            minHeap.poll()
        }

        minHeap.add(meeting)
        minRooms = max(minRooms, minHeap.size)
    }
    return minRooms
}



