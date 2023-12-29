package com.imashnake.animite.api.anilist.markdown

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

fun ASTNode.toAnnotatedString(text: String): AnnotatedString = buildAnnotatedString {
    this@toAnnotatedString.traverse { currentNode ->
        when (currentNode.type) {
            MarkdownElementTypes.EMPH -> {
                if (currentNode.children.size == 3) {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(currentNode.children[1].getTextInNode(text).toString())
                    }
                }
            }

            MarkdownTokenTypes.TEXT, MarkdownTokenTypes.WHITE_SPACE -> {
                append(currentNode.getTextInNode(text).toString())
            }
        }
        currentNode.run {
            Log.d(
                "ASTNodeExt",
                "\"${getTextInNode(text)}\" ||||| $startOffset - $endOffset ||||| ${type.name}"
            )
        }
    }
}

fun ASTNode.traverse(
    action: (currentNode: ASTNode) -> Unit
) {
    val queue = ArrayDeque<ASTNode>()
    queue.addFirst(this)

    while(queue.isNotEmpty()) {
        val currentNode = queue.removeLast()

        action.invoke(currentNode)

        for(childNode in currentNode.children) {
            queue.addFirst(childNode)
        }
    }
}
