package com.imashnake.animite.features.searchbar

/**
 * UI model for [SearchList] items.
 *
 * @param id unique identifier for the item.
 * @param image URL of the image associated with the item.
 * @param title title of the media.
 * @param seasonYear the season and year the media was released in, e.g., "WINTER 2023".
 * @param studios list of studios separated by commas.
 * @param format format the media was released in.
 * @param episodes the amount of episodes the anime has when complete.
 *
 * @property footer constructs the footer based on the nullability of [format] and [episodes].
 */
data class SearchItem(
    val id: Int,
    val image: String? = null,
    val title: String? = null,
    val seasonYear: String? = null,
    val studios: String? = null,
    val format: String? = null,
    val episodes: Int? = null
) {
    /**
     * Constructs the footer based on the nullability of [format] and [episodes] (and hence
     * [suffixedEpisodes]).
     * @return e.g., "TV ꞏ 12 episodes", "MOVIE", "1 episode", etc.
     */
    fun footer(suffixedEpisodes: String?): String = listOfNotNull(
        format,
        suffixedEpisodes
    ).joinToString(separator = " ꞏ ")
}
