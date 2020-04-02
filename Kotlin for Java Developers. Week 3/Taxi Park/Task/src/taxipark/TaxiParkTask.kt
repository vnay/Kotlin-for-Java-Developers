package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers - trips.map { it.driver }

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        allPassengers.filter { passenger -> trips.count { passenger in it.passengers} >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips
            .filter { trip -> trip.driver == driver }
            .flatMap { trip -> trip.passengers }
            .groupingBy { passenger -> passenger }
            .eachCount()
            .filter { entry -> entry.value > 1 }
            .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val (withDiscount, withoutDiscount) = trips.partition { it.discount != null }

    return allPassengers
            .filter { passenger ->
                withDiscount.count { passenger in it.passengers } >
                        withoutDiscount.count { passenger in it.passengers }
            }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val ids = trips.groupBy { trip -> trip.duration / 10 }
            .map { group -> group.key to group.value.count() }
            .maxBy { pair -> pair.second }
            ?.first

    val min = ids?.times(10)
    val max = min?.plus(9) ?: 0

    return min?.rangeTo(max)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if(trips.isEmpty()) return false

    val totalIncome = trips.sumByDouble(Trip::cost)

    val sortedDrivers = trips
            .groupBy(Trip::driver)
            .map { (_, tripByDriver) -> tripByDriver.sumByDouble(Trip::cost) }
            .sortedDescending()

    val topDrivers = (0.2 * allDrivers.size).toInt()

    val incomeByTopDrivers = sortedDrivers
            .take(topDrivers)
            .sum()

    return incomeByTopDrivers >= 0.8 * totalIncome
}