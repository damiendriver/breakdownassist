package ie.setu.breakdownassist.models

interface BreakdownStore {
    fun findAll(): List<BreakdownModel>
    fun create(breakdown: BreakdownModel)
    fun update(breakdown: BreakdownModel)
}