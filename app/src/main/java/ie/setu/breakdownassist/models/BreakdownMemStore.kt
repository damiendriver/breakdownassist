package ie.setu.breakdownassist.models

import timber.log.Timber.Forest.i

class BreakdownMemStore : BreakdownStore {

    val breakdowns = ArrayList<BreakdownModel>()

    override fun findAll(): List<BreakdownModel> {
        return breakdowns
    }

    override fun create(breakdown: BreakdownModel) {
        breakdowns.add(breakdown)
        logAll()
    }

    fun logAll() {
        breakdowns.forEach{ i("${it}") }
    }
}