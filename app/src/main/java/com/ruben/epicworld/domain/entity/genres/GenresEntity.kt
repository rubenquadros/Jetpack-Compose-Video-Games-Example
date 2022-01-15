package com.ruben.epicworld.domain.entity.genres

/**
 * Created by Ruben Quadros on 15/01/22
 **/
data class GenresEntity(val genres: List<GenresResultEntity>) {
    constructor(): this(arrayListOf())
}