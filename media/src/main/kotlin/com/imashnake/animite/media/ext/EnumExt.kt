package com.imashnake.animite.media.ext

import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.media.R

val Media.InfoItem.title get() = when(this) {
    Media.InfoItem.FORMAT -> R.string.format
    Media.InfoItem.EPISODES -> R.string.episodes
    Media.InfoItem.DURATION -> R.string.duration
    Media.InfoItem.STATUS -> R.string.status
    Media.InfoItem.START_DATE -> R.string.start_date
    Media.InfoItem.END_DATE -> R.string.end_date
    Media.InfoItem.SEASON -> R.string.season
    Media.InfoItem.STUDIO -> R.string.studios
    Media.InfoItem.SOURCE -> R.string.source
    Media.InfoItem.DIVIDER -> null
}

val Media.Format.res get() = when(this) {
    Media.Format.TV -> R.string.tv
    Media.Format.TV_SHORT -> R.string.tv_short
    Media.Format.MOVIE -> R.string.movie
    Media.Format.SPECIAL -> R.string.special
    Media.Format.OVA -> R.string.ova
    Media.Format.ONA -> R.string.ona
    Media.Format.MUSIC -> R.string.music
    Media.Format.MANGA -> R.string.manga
    Media.Format.NOVEL -> R.string.novel
    Media.Format.ONE_SHOT -> R.string.one_shot
}

val Media.Status.res get() = when(this) {
    Media.Status.FINISHED -> R.string.finished
    Media.Status.RELEASING -> R.string.releasing
    Media.Status.NOT_YET_RELEASED -> R.string.not_released
    Media.Status.CANCELLED -> R.string.cancelled
    Media.Status.HIATUS -> R.string.hiatus
}

val Media.Season.res get() = when(this) {
    Media.Season.WINTER -> R.string.winter
    Media.Season.SPRING -> R.string.spring
    Media.Season.SUMMER -> R.string.summer
    Media.Season.FALL -> R.string.fall
}

val Media.Season.icon get() = when(this) {
    Media.Season.WINTER -> R.drawable.winter
    Media.Season.SPRING -> R.drawable.spring
    Media.Season.SUMMER -> R.drawable.summer
    Media.Season.FALL -> R.drawable.fall
}

val Media.Source.res get() = when(this) {
    Media.Source.ORIGINAL -> R.string.original
    Media.Source.MANGA -> R.string.manga
    Media.Source.LIGHT_NOVEL -> R.string.light_novel
    Media.Source.VISUAL_NOVEL -> R.string.visual_novel
    Media.Source.VIDEO_GAME -> R.string.video_game
    Media.Source.OTHER -> R.string.other
    Media.Source.NOVEL -> R.string.novel
    Media.Source.DOUJINSHI -> R.string.doujinshi
    Media.Source.ANIME -> R.string.anime
    Media.Source.WEB_NOVEL -> R.string.web_novel
    Media.Source.LIVE_ACTION -> R.string.live_action
    Media.Source.GAME -> R.string.game
    Media.Source.COMIC -> R.string.comic
    Media.Source.MULTIMEDIA_PROJECT -> R.string.multimedia_project
    Media.Source.PICTURE_BOOK -> R.string.picture_book
}

val Media.Relation.res get() = when(this) {
    Media.Relation.ADAPTATION -> R.string.adaptation_relation
    Media.Relation.PREQUEL -> R.string.prequel_relation
    Media.Relation.SEQUEL -> R.string.sequel_relation
    Media.Relation.PARENT -> R.string.parent_relation
    Media.Relation.SIDE_STORY -> R.string.side_story_relation
    Media.Relation.CHARACTER -> R.string.character_relation
    Media.Relation.SUMMARY -> R.string.summary_relation
    Media.Relation.ALTERNATIVE -> R.string.alternative_relation
    Media.Relation.SPIN_OFF -> R.string.spin_off_relation
    Media.Relation.OTHER -> R.string.other_relation
    Media.Relation.SOURCE -> R.string.source_relation
    Media.Relation.COMPILATION -> R.string.compilation_relation
    Media.Relation.CONTAINS -> R.string.contains_relation
}

val Media.Ranking.TimeSpan.res get() = when(this) {
    Media.Ranking.TimeSpan.ALL_TIME -> R.string.all_time
    Media.Ranking.TimeSpan.YEAR -> R.string.year
    Media.Ranking.TimeSpan.SEASON -> R.string.season
}
