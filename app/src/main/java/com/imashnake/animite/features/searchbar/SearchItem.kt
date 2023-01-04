package com.imashnake.animite.features.searchbar

/**
 * UI model for [SearchList] items.
 *
 * @param id unique identifier for the item.
 * @param image URL of the image associated with the item.
 * @param title title of the media.
 * @param seasonYear the season and year the media was released in, e.g., "WINTER 2023".
 * @param studios list of studios separated by commas.
 * @param footer metadata with the format and number of episodes (if applicable).
 */
data class SearchItem(
    val id: Int,
    val image: String? = null,
    val title: String? = null,
    val seasonYear: String? = null,
    val studios: String? = null,
    val footer: String? = null
)
