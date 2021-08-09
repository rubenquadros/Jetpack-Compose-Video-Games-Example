package com.ruben.epicworld.remote.model.response.gamedetails

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruben Quadros on 06/08/21
 **/
data class Reactions(
    @SerializedName("1")
    val one : Int,
    @SerializedName("2")
    val two : Int,
    @SerializedName("3")
    val three : Int,
    @SerializedName("4")
    val four : Int,
    @SerializedName("5")
    val five : Int,
    @SerializedName("6")
    val six : Int,
    @SerializedName("7")
    val seven : Int,
    @SerializedName("8")
    val eight : Int,
    @SerializedName("9")
    val nine : Int,
    @SerializedName("10")
    val ten : Int,
    @SerializedName("11")
    val eleven : Int,
    @SerializedName("12")
    val twelve : Int,
    @SerializedName("14")
    val fourteen : Int,
    @SerializedName("15")
    val fifteen : Int,
    @SerializedName("16")
    val sixteen : Int,
    @SerializedName("18")
    val eighteen : Int,
    @SerializedName("20")
    val twenty : Int,
    @SerializedName("21")
    val twentyOne : Int
) {
    constructor(): this(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
}
