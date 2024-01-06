package com.imashnake.animite.profile.dev.internal

import com.imashnake.animite.profile.BuildConfig

const val ANILIST_AUTH_DEEPLINK = "jinnah://animite#access_token={accessToken}&token_type=Bearer&expires_in=31622400"
const val ANILIST_AUTH_URL = "https://anilist.co/api/v2/oauth/authorize?client_id={${BuildConfig.CLIENT_ID}}&response_type=token"
