package com.imashnake.animite.api.anilist.markdown

import android.util.Log
import androidx.compose.ui.text.buildAnnotatedString
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode

fun ASTNode.toAnnotatedString() {
    this.traverse { type, start, end ->
        if (end != this.endOffset) {
            buildAnnotatedString {

            }
        }
        Log.d("ASTNodeExt", "$start - $end; ${type.name}")
    }
}

fun ASTNode.traverse(
    action: (type: IElementType, startOffset : Int, endOffset : Int) -> Unit
) {
    val queue = ArrayDeque<ASTNode>()
    queue.addFirst(this)

    while(queue.isNotEmpty()) {
        val currentNode = queue.removeLast()

        action.invoke(currentNode.type, currentNode.startOffset, currentNode.endOffset)

        for(childNode in currentNode.children) {
            queue.addFirst(childNode)
        }
    }
}
