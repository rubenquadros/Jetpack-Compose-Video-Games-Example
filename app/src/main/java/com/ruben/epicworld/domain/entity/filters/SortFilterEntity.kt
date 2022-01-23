package com.ruben.epicworld.domain.entity.filters

/**
 * Created by Ruben Quadros on 15/01/22
 **/
data class SortFilterEntity(
    val name: String,
    val value: String,
    var isSelected: Boolean = false
)
