package com.ruben.epicworld.domain.entity.filters

/**
 * Created by Ruben Quadros on 15/01/22
 **/
data class GamesFilterEntity(
    val type: FilterType,
    val selectedFilters: List<Any>
)

enum class FilterType {
    SORT_ORDER, PLATFORMS, GENRES, NONE
}