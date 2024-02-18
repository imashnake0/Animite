package com.nasdroid.core.markdown

import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownBlockQuote
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownCodeBlock
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownCodeSpan
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownHeader
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownImage
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownOrderedList
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownParagraph
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownTable
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownTableColumn
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownText
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownUnorderedList
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownLink
import com.nasdroid.core.markdown.MarkdownNodeBuilders.markdownListItem
import com.nasdroid.core.markdown.generator.MarkdownEol
import com.nasdroid.core.markdown.generator.MarkdownHeading
import com.nasdroid.core.markdown.generator.MarkdownNode
import com.nasdroid.core.markdown.generator.MarkdownNodeGenerator
import com.nasdroid.core.markdown.generator.MarkdownParagraph
import com.nasdroid.core.markdown.generator.MarkdownRule
import com.nasdroid.core.markdown.generator.MarkdownSpanNode
import com.nasdroid.core.markdown.generator.MarkdownTable
import com.nasdroid.core.markdown.generator.MarkdownWhitespace
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownNodeGeneratorTest {

    companion object {
        private val ITALICS_PATTERNS = mapOf(
            "*asterisks*" to markdownText(text = "asterisks", isItalics = true),
            "_underscores_" to markdownText(text = "underscores", isItalics = true),
            "*multi word asterisks*" to markdownText(text = "multi word asterisks", isItalics = true),
            "_multi word underscores_" to markdownText(text = "multi word underscores", isItalics = true),
        )

        private val BOLD_PATTERNS = mapOf(
            "**asterisks**" to markdownText(text = "asterisks", isBold = true),
            "__underscores__" to markdownText(text = "underscores", isBold = true),
            "**multi word asterisks**" to markdownText(text = "multi word asterisks", isBold = true),
            "__multi word underscores__" to markdownText(text = "multi word underscores", isBold = true),
        )

        private val STRIKETHROUGH_PATTERNS = mapOf(
            "~~strikethrough~~" to markdownText(text = "strikethrough", isStrikethrough = true),
            "~~multi word strikethrough~~" to
                    markdownText(text = "multi word strikethrough", isStrikethrough = true)
        )

        private val MONOSPACE_PATTERNS = mapOf(
            "`monospace`" to markdownCodeSpan("monospace"),
            "` `" to markdownCodeSpan(" "),
            "`multi word monospace`" to markdownCodeSpan("multi word monospace")
        )

        private val RULE_PATTERNS = mapOf(
            "---" to MarkdownRule
        )

        private val CODEBLOCK_PATTERNS = mapOf(
            """
                ```kotlin
                val codeBlockTest = true
                ```
            """.trimIndent() to markdownCodeBlock(text = "val codeBlockTest = true", language = "kotlin"),
            """
                ```kotlin
                val codeBlockTest = true
                val secondLine = true
                ```
            """.trimIndent() to markdownCodeBlock(
                text = "val codeBlockTest = true\nval secondLine = true",
                language = "kotlin"
            ),
            """
                ```kotlin
                val codeBlockTest = true
                
                val secondLine = true
                ```
            """.trimIndent() to markdownCodeBlock(
                text = "val codeBlockTest = true\n\nval secondLine = true",
                language = "kotlin"
            ),
        )

        private val BLOCKQUOTE_PATTERNS = mapOf(
            "> this is a block quote" to markdownBlockQuote(
                markdownParagraph(
                    markdownText("this is a block quote")
                )
            ),
            """
                > > What on earth
                >
                > > Is this
                >
                > # Heading
            """.trimIndent() to markdownBlockQuote(
                markdownBlockQuote(
                    markdownParagraph(markdownText("What on earth"))
                ),
                markdownBlockQuote(
                    markdownParagraph(markdownText("Is this"))
                ),
                markdownHeader(MarkdownHeading.Size.Headline1, markdownText("Heading")),
            )
        )

        private val PARAGRAPH_PATTERNS = mapOf(
            "**bold** _italics_ ~~strikethrough~~ [Google](https://google.com)" to markdownParagraph(
                markdownText("bold", isBold = true),
                MarkdownWhitespace,
                markdownText("italics", isItalics = true),
                MarkdownWhitespace,
                markdownText("strikethrough", isStrikethrough = true),
                MarkdownWhitespace,
                markdownLink("https://google.com", listOf(markdownText("Google")))
            ),
            """
                **bold** _italics_
                [Google](https://google.com)
                This is a single newline test
            """.trimIndent() to markdownParagraph(
                markdownText("bold", isBold = true),
                MarkdownWhitespace,
                markdownText("italics", isItalics = true),
                MarkdownEol,
                markdownLink("https://google.com", listOf(markdownText("Google"))),
                MarkdownEol,
                markdownText("This is a single newline test")
            ),
            "un*frigging*believable" to markdownParagraph(
                markdownText("un"),
                markdownText("frigging", isItalics = true),
                markdownText("believable")
            )
        )

        private val LINK_PATTERNS = mapOf(
            "https://google.com" to markdownLink("https://google.com"),
            "<https://google.com>" to markdownLink("https://google.com"),
            "[Google](https://google.com)" to markdownLink(
                link = "https://google.com",
                text = listOf(markdownText("Google"))
            ),
            "[*Google*](https://google.com)" to markdownLink(
                link = "https://google.com",
                text = listOf(markdownText("Google", isItalics = true))
            ),
            "[**Google**](https://google.com)" to markdownLink(
                link = "https://google.com",
                text = listOf(markdownText("Google", isBold = true))
            ),
            "[Google](https://google.com \"Google\")" to markdownLink(
                link = "https://google.com",
                text = listOf(markdownText("Google")),
                altText = "Google"
            ),
        )

        private val PLAINTEXT_PATTERNS = mapOf(
            "text" to markdownText(text = "text"),
            "one two three" to markdownText("one two three")
        )

        private val HEADER_PATTERNS = mapOf(
            """
                H1
                ===
            """.trimIndent() to markdownHeader(MarkdownHeading.Size.Headline1, markdownText("H1")),
            "# H1" to markdownHeader(MarkdownHeading.Size.Headline1, markdownText("H1")),
            """
                H2
                ---
            """.trimIndent() to markdownHeader(MarkdownHeading.Size.Headline2, markdownText("H2")),
            "## H2" to markdownHeader(MarkdownHeading.Size.Headline2, markdownText("H2")),
            "### H3" to markdownHeader(MarkdownHeading.Size.Headline3, markdownText("H3")),
            "#### H4" to markdownHeader(MarkdownHeading.Size.Headline4, markdownText("H4")),
            "##### H5" to markdownHeader(MarkdownHeading.Size.Headline5, markdownText("H5")),
            "###### H6" to markdownHeader(MarkdownHeading.Size.Headline6, markdownText("H6")),
        )

        private val IMAGE_PATTERNS = mapOf(
            "![alt text](https://test.image)" to markdownImage("alt text", "https://test.image"),
            "![alt text](https://test.image \"Test image\")" to markdownImage("alt text", "https://test.image", "Test image")
        )

        private val TABLE_PATTERNS = mapOf(
            """
                | foo | bar |
                | --- | --- |
                | baz | bim |
            """.trimIndent() to markdownTable(
                markdownTableColumn(
                    markdownParagraph(markdownText("foo")),
                    MarkdownTable.Alignment.LEFT,
                    markdownParagraph(markdownText("baz"))
                ),
                markdownTableColumn(
                    markdownParagraph(markdownText("bar")),
                    MarkdownTable.Alignment.LEFT,
                    markdownParagraph(markdownText("bim"))
                )
            ),
            """
                | foo | bar | baz |
                | :--- | ---: | :---: |
            """.trimIndent() to markdownTable(
                markdownTableColumn(
                    markdownParagraph(markdownText("foo")),
                    MarkdownTable.Alignment.LEFT,
                ),
                markdownTableColumn(
                    markdownParagraph(markdownText("bar")),
                    MarkdownTable.Alignment.RIGHT,
                ),
                markdownTableColumn(
                    markdownParagraph(markdownText("baz")),
                    MarkdownTable.Alignment.CENTER,
                )
            )
        )

        private val UNORDERED_LIST_PATTERNS = mapOf(
            """
                * Apples
                * Oranges
                * Pears
            """.trimIndent() to markdownUnorderedList(
                markdownListItem(markdownParagraph(markdownText("Apples"))),
                markdownListItem(markdownParagraph(markdownText("Oranges"))),
                markdownListItem(markdownParagraph(markdownText("Pears"))),
            ),
            """
                - Apples
                - Oranges
                - Pears
            """.trimIndent() to markdownUnorderedList(
                markdownListItem(markdownParagraph(markdownText("Apples"))),
                markdownListItem(markdownParagraph(markdownText("Oranges"))),
                markdownListItem(markdownParagraph(markdownText("Pears"))),
            ),
            """
                + Apples
                + Oranges
                + Pears
            """.trimIndent() to markdownUnorderedList(
                markdownListItem(markdownParagraph(markdownText("Apples"))),
                markdownListItem(markdownParagraph(markdownText("Oranges"))),
                markdownListItem(markdownParagraph(markdownText("Pears"))),
            ),
        )

        private val ORDERED_LIST_PATTERNS = mapOf(
            """
                1. Apples
                2. Oranges
                3. Pears
            """.trimIndent() to markdownOrderedList(
                markdownListItem(markdownParagraph(markdownText("Apples"))),
                markdownListItem(markdownParagraph(markdownText("Oranges"))),
                markdownListItem(markdownParagraph(markdownText("Pears"))),
            ),
            """
                1) Apples
                2) Oranges
                3) Pears
            """.trimIndent() to markdownOrderedList(
                markdownListItem(markdownParagraph(markdownText("Apples"))),
                markdownListItem(markdownParagraph(markdownText("Oranges"))),
                markdownListItem(markdownParagraph(markdownText("Pears"))),
            ),
        )

        private val MIXED_LIST_PATTERNS = mapOf(
            """
                * Mixed
                    1. List
                        + Containing
                    2. Different
                * Types
            """.trimIndent() to markdownUnorderedList(
                markdownListItem(
                    markdownParagraph(markdownText("Mixed")),
                    markdownOrderedList(
                        markdownListItem(
                            markdownParagraph(markdownText("List")),
                            markdownUnorderedList(
                                markdownListItem(markdownParagraph(markdownText("Containing")))
                            )
                        ),
                        markdownListItem(markdownParagraph(markdownText("Different")))
                    )
                ),
                markdownListItem(markdownParagraph(markdownText("Types")))
            )
        )

        private fun produceMarkdownAstNode(markdown: String): ASTNode {
            val flavor = GFMFlavourDescriptor()
            val tree = MarkdownParser(flavor).buildMarkdownTreeFromString(markdown)
            return tree
        }
    }

    private fun testSpanParsing(markdown: String, expectedText: MarkdownSpanNode) {
        testNodeParsing(markdown, MarkdownParagraph(listOf(expectedText)))
    }

    private fun testNodeParsing(markdown: String, expectedNode: MarkdownNode) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedNode), actual,
            "Matching failed for input $markdown"
        )
    }

    @Test
    fun `plaintext parsing`() {
        PLAINTEXT_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }
    @Test
    fun `strikethrough parsing`() {
        STRIKETHROUGH_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `bold parsing`() {
        BOLD_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `italics parsing`() {
        ITALICS_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `header parsing`() {
        HEADER_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `link parsing`() {
        LINK_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `paragraph parsing`() {
        PARAGRAPH_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `image parsing`() {
        IMAGE_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `rule parsing`() {
        RULE_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `blockquote parsing`() {
        BLOCKQUOTE_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `code span parsing`() {
        MONOSPACE_PATTERNS.forEach { (markdown, expected) ->
            testSpanParsing(markdown, expected)
        }
    }

    @Test
    fun `code block parsing`() {
        CODEBLOCK_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `table parsing`() {
        TABLE_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `unordered list parsing`() {
        UNORDERED_LIST_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `mixed list parsing`() {
        MIXED_LIST_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }

    @Test
    fun `ordered list parsing`() {
        ORDERED_LIST_PATTERNS.forEach { (markdown, expected) ->
            testNodeParsing(markdown, expected)
        }
    }
}
