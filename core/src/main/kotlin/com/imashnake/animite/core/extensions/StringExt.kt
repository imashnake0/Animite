package com.imashnake.animite.core.extensions

fun String.addNewlineAfterParagraph() = replace("</p>", "</p><br>")
