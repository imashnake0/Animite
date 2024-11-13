package com.imashnake.animite.profile.dev.internal

const val ACCESS_TOKEN = "accessToken"
const val TOKEN_TYPE = "tokenType"
const val EXPIRES_IN = "expiresIn"
const val ANILIST_AUTH_DEEPLINK = "app://animite#access_token={$ACCESS_TOKEN}&token_type={$TOKEN_TYPE}&expires_in={$EXPIRES_IN}"

const val ANILIST_AUTH_URL = "https://anilist.co/api/v2/oauth/authorize?client_id=17716&response_type=token"
