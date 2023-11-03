package ie.setu.breakdownassist.models

import timber.log.Timber.Forest.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class BreakdownMemStore : BreakdownStore {

    val breakdowns = ArrayList<BreakdownModel>()

    override fun findAll(): List<BreakdownModel> {
        return breakdowns
    }

    override fun create(breakdown: BreakdownModel) {
        breakdown.id = getId()
        breakdowns.add(breakdown)
        logAll()
    }

    override fun update(breakdown: BreakdownModel) {
        var foundBreakdown: BreakdownModel? = breakdowns.find { p -> p.id == breakdown.id }
        if (foundBreakdown != null) {
            foundBreakdown.title = breakdown.title
            foundBreakdown.description = breakdown.description
            foundBreakdown.phone = breakdown.phone
            foundBreakdown.image = breakdown.image
            foundBreakdown.lat = breakdown.lat
            foundBreakdown.lng = breakdown.lng
            foundBreakdown.zoom = breakdown.zoom
            logAll()
        }
    }

    private fun logAll() {
        breakdowns.forEach { i("$it") }
    }

    override fun delete(breakdown: BreakdownModel) {
        breakdowns.remove(breakdown)
    }
}