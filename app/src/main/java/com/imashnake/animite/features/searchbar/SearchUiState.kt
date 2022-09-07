package com.imashnake.animite.features.searchbar

import com.imashnake.animite.SearchQuery

data class SearchUiState(
    val searchList: SearchQuery.Page? = null
)
