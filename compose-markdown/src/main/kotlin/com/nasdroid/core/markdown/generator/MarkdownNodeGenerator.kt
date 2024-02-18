package com.nasdroid.core.markdown.generator

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes

/**
 * A class that takes a Markdown document and its parsed [ASTNode], and produces a type-safe
 * representation of the document tree. See [MarkdownNode] for possible types.
 */
class MarkdownNodeGenerator(
    private val allFileText: String,
    private val rootNode: ASTNode
) {
    init {
        check(rootNode.type == MarkdownElementTypes.MARKDOWN_FILE) {
            "The root node provided must be of type MARKDOWN_FILE, but it was ${rootNode.type}"
        }
    }

    /**
     * Generates a list of [MarkdownNode]s from the data provided at construction time.
     */
    fun generateNodes(): List<MarkdownNode> {
        return rootNode.children.mapNotNull { parseGenericNode(it) }
    }

    private fun parseGenericNode(astNode: ASTNode): MarkdownNode? {
        return when (astNode.type) {
            MarkdownElementTypes.ATX_1,
            MarkdownElementTypes.ATX_2,
            MarkdownElementTypes.ATX_3,
            MarkdownElementTypes.ATX_4,
            MarkdownElementTypes.ATX_5,
            MarkdownElementTypes.ATX_6,
            MarkdownElementTypes.SETEXT_1,
            MarkdownElementTypes.SETEXT_2 -> parseHeaderNode(astNode)
            MarkdownElementTypes.PARAGRAPH -> parseParagraphNode(astNode)
            MarkdownElementTypes.BLOCK_QUOTE -> parseBlockQuote(astNode)
            MarkdownTokenTypes.EOL,
            MarkdownTokenTypes.WHITE_SPACE -> null // Ignored in generic nodes, the renderer should handle it.
            MarkdownTokenTypes.HORIZONTAL_RULE -> MarkdownRule
            MarkdownElementTypes.CODE_BLOCK,
            MarkdownElementTypes.CODE_FENCE -> parseCodeBlock(astNode)
            GFMElementTypes.TABLE -> parseTable(astNode)
            MarkdownElementTypes.UNORDERED_LIST -> parseUnorderedList(astNode)
            MarkdownElementTypes.ORDERED_LIST -> parseOrderedList(astNode)
            MarkdownElementTypes.HTML_BLOCK -> MarkdownHtmlBlock(astNode.getTextInNode(allFileText).toString())
            else -> error("Unknown node type ${astNode.type}")
        }
    }

    private fun parseUnorderedList(astNode: ASTNode): MarkdownUnorderedList {
        val listItems = astNode.children
            .filter { it.type == MarkdownElementTypes.LIST_ITEM }
            .map { listItemNode ->
                MarkdownListItem(
                    content = listItemNode.children.drop(1)
                        .mapNotNull { parseGenericNode(it) }
                )
            }
        return MarkdownUnorderedList(listItems)
    }

    private fun parseOrderedList(astNode: ASTNode): MarkdownOrderedList {
        val list = parseUnorderedList(astNode)
        return MarkdownOrderedList(list.listItems)
    }

    private fun parseTable(astNode: ASTNode): MarkdownTable {
        val headers = astNode.children[0].children.filter { it.type == GFMTokenTypes.CELL }
        val bodyNodes = astNode.children
            .filter { it.type == GFMElementTypes.ROW }
        val columns = headers.mapIndexed { index: Int, headerNode: ASTNode ->
            val columnCellNodes = bodyNodes
                .map { row -> row.children.filter { it.type == GFMTokenTypes.CELL }[index] }
            MarkdownTable.Column(
                header = parseParagraphNode(headerNode),
                alignment = MarkdownTable.Alignment.LEFT,
                cells = columnCellNodes.map { parseParagraphNode(it) }
            )
        }
        return MarkdownTable(columns)
    }

    private fun parseCodeBlock(astNode: ASTNode): MarkdownCodeBlock {
        val language = astNode.children
            .firstOrNull { it.type == MarkdownTokenTypes.FENCE_LANG }
            ?.getTextInNode(allFileText)
            ?.toString()
        val code = astNode.children
            .filterNot {
                it.type == MarkdownTokenTypes.CODE_FENCE_START ||
                        it.type == MarkdownTokenTypes.CODE_FENCE_END ||
                        it.type == MarkdownTokenTypes.FENCE_LANG
            }
            .joinToString(separator = "") { node ->
                if (node.type == MarkdownTokenTypes.EOL) {
                    "\n"
                } else {
                    node.getTextInNode(allFileText)
                }
            }
            .trim('\n')
            .trimIndent()
        return MarkdownCodeBlock(code, language)
    }

    private fun parseBlockQuote(astNode: ASTNode): MarkdownBlockQuote {
        return MarkdownBlockQuote(
            children = astNode.children
                .filterNot { it.type == MarkdownTokenTypes.BLOCK_QUOTE }
                .mapNotNull { parseGenericNode(it) }
        )
    }

    private fun parseParagraphNode(astNode: ASTNode): MarkdownParagraph {
        val parsedChildren = astNode.children
            .dropWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
            .dropLastWhile { it.type == MarkdownTokenTypes.WHITE_SPACE }
            .map { childNode -> parseSpanNode(childNode) }
        return MarkdownParagraph(children = parsedChildren)
    }

    private fun parseSpanNode(astNode: ASTNode): MarkdownSpanNode {
        return when (astNode.type) {
            MarkdownElementTypes.STRONG,
            GFMElementTypes.STRIKETHROUGH,
            MarkdownTokenTypes.TEXT,
            MarkdownTokenTypes.SETEXT_CONTENT,
            MarkdownTokenTypes.ATX_CONTENT,
            MarkdownTokenTypes.HTML_TAG,
            MarkdownTokenTypes.HTML_BLOCK_CONTENT,
            MarkdownTokenTypes.COLON,
            MarkdownElementTypes.EMPH -> parseTextNode(astNode)
            MarkdownTokenTypes.WHITE_SPACE -> MarkdownWhitespace
            MarkdownTokenTypes.EOL -> MarkdownEol
            GFMTokenTypes.GFM_AUTOLINK,
            MarkdownElementTypes.INLINE_LINK,
            MarkdownElementTypes.AUTOLINK -> parseLinkNode(astNode)
            MarkdownElementTypes.IMAGE -> parseImageNode(astNode)
            MarkdownElementTypes.CODE_SPAN -> parseCodeSpan(astNode)
            else -> error("Unsure how to handle type ${astNode.type} inside a PARAGRAPH")
        }
    }
    private fun parseCodeSpan(astNode: ASTNode): MarkdownCodeSpan {
        val text = astNode.children
            .filterNot { it.type == MarkdownTokenTypes.BACKTICK }
            .joinToString(separator = "") { it.getTextInNode(allFileText) }
        return MarkdownCodeSpan(text)
    }

    private fun parseImageNode(astNode: ASTNode): MarkdownImage {
        val imageLink = astNode.children[1]
        val link = parseLinkNode(imageLink)

        return MarkdownImage(
            imageUrl = link.url,
            contentDescription = link.displayText.joinToString(separator = " ") { (it as MarkdownText).text },
            titleText = link.titleText
        )
    }

    private fun parseHeaderNode(astNode: ASTNode): MarkdownHeading {
        return when (astNode.type) {
            MarkdownElementTypes.SETEXT_1,
            MarkdownElementTypes.ATX_1 -> MarkdownHeading(
                children = astNode.children
                    .filterNot {
                        it.type == MarkdownTokenTypes.SETEXT_1 ||
                                it.type == MarkdownTokenTypes.EOL ||
                                it.type == MarkdownTokenTypes.ATX_HEADER
                    }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline1,
            )
            MarkdownElementTypes.SETEXT_2,
            MarkdownElementTypes.ATX_2 -> MarkdownHeading(
                children = astNode.children
                    .filterNot {
                        it.type == MarkdownTokenTypes.SETEXT_2 ||
                                it.type == MarkdownTokenTypes.EOL ||
                                it.type == MarkdownTokenTypes.ATX_HEADER
                    }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline2,
            )
            MarkdownElementTypes.ATX_3 -> MarkdownHeading(
                children = astNode.children
                    .filterNot { it.type == MarkdownTokenTypes.ATX_HEADER }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline3,
            )
            MarkdownElementTypes.ATX_4 -> MarkdownHeading(
                children = astNode.children
                    .filterNot { it.type == MarkdownTokenTypes.ATX_HEADER }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline4,
            )
            MarkdownElementTypes.ATX_5 -> MarkdownHeading(
                children = astNode.children
                    .filterNot { it.type == MarkdownTokenTypes.ATX_HEADER }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline5,
            )
            MarkdownElementTypes.ATX_6 -> MarkdownHeading(
                children = astNode.children
                    .filterNot { it.type == MarkdownTokenTypes.ATX_HEADER }
                    .map { parseSpanNode(it) },
                size = MarkdownHeading.Size.Headline6,
            )
            else -> error("Unsure how to handle header type ${astNode.type}")
        }
    }

    private fun parseTextNode(astNode: ASTNode): MarkdownText {
        return when (astNode.type) {
            MarkdownTokenTypes.SETEXT_CONTENT,
            MarkdownTokenTypes.ATX_CONTENT,
            MarkdownTokenTypes.HTML_TAG,
            MarkdownTokenTypes.HTML_BLOCK_CONTENT,
            MarkdownTokenTypes.COLON,
            MarkdownTokenTypes.TEXT -> MarkdownText(
                text = astNode.getTextInNode(allFileText).trim().toString(),
                isBold = false,
                isItalics = false,
                isStrikethrough = false
            )
            MarkdownElementTypes.STRONG,
            GFMElementTypes.STRIKETHROUGH,
            MarkdownElementTypes.EMPH -> MarkdownText(
                text = astNode.children
                    .filter { it.type == MarkdownTokenTypes.TEXT || it.type == MarkdownTokenTypes.WHITE_SPACE }
                    .joinToString { it.getTextInNode(allFileText) },
                isBold = astNode.type == MarkdownElementTypes.STRONG,
                isItalics = astNode.type == MarkdownElementTypes.EMPH,
                isStrikethrough = astNode.type == GFMElementTypes.STRIKETHROUGH,
            )
            else -> error("Unsure how to handle text type ${astNode.type} ")
        }
    }

    private fun parseLinkNode(astNode: ASTNode): MarkdownLink {
        return when (astNode.type) {
            MarkdownElementTypes.AUTOLINK -> {
                val url = astNode.children
                    .filter { it.type == MarkdownTokenTypes.AUTOLINK }
                    .joinToString(separator = "") { it.getTextInNode(allFileText) }
                MarkdownLink(
                    displayText = listOf(
                        MarkdownText(
                            text = url,
                            isBold = false,
                            isItalics = false,
                            isStrikethrough = false
                        )
                    ),
                    url = url,
                    titleText = null
                )
            }
            MarkdownElementTypes.INLINE_LINK -> {
                val link = astNode.children
                    .first { it.type == MarkdownElementTypes.LINK_DESTINATION }
                    .getTextInNode(allFileText)
                val label = astNode.children
                    .first { it.type == MarkdownElementTypes.LINK_TEXT }
                    .children
                    .filterNot { it.type == MarkdownTokenTypes.LBRACKET || it.type == MarkdownTokenTypes.RBRACKET }
                    .map { parseSpanNode(it) }
                val titleText = astNode.children
                    .firstOrNull { it.type == MarkdownElementTypes.LINK_TITLE }
                    ?.children
                    ?.first { it.type == MarkdownTokenTypes.TEXT }
                    ?.getTextInNode(allFileText)
                MarkdownLink(
                    displayText = label,
                    url = link.toString(),
                    titleText = titleText?.toString()
                )
            }
            GFMTokenTypes.GFM_AUTOLINK -> {
                val url = astNode.getTextInNode(allFileText).toString()
                MarkdownLink(
                    displayText = listOf(
                        MarkdownText(
                            text = url,
                            isBold = false,
                            isItalics = false,
                            isStrikethrough = false
                        )
                    ),
                    url = url,
                    titleText = null
                )
            }
            else -> {
                error("Unsure how to handle link type ${astNode.type}")
            }
        }
    }
}
