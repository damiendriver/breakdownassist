package ie.setu.breakdownassist.api

import ie.setu.breakdownassist.models.BreakdownModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BreakdownService {
    @GET("/breakdowns")
    fun getall(): Call<List<BreakdownModel>>

    @GET("/donations/{id}")
    fun get(@Path("id") id: String): Call<BreakdownModel>

    @DELETE("/donations/{id}")
    fun delete(@Path("id") id: String): Call<BreakdownWrapper>

    @POST("/donations")
    fun post(@Body breakdown: BreakdownModel): Call<BreakdownWrapper>

    @PUT("/donations/{id}")
    fun put(
        @Path("id") id: String,
        @Body breakdown: BreakdownModel
    ): Call<BreakdownWrapper>
}
