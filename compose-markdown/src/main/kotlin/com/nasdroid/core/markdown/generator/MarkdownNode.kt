package com.nasdroid.core.markdown.generator

/**
 * Encapsulates all possible content types within a Markdown document.
 */
sealed interface MarkdownNode

/**
 * Encapsulates all possible span nodes in a Markdown document. A span node is a stylized element
 * contained within a block of text, like a paragraph.
 */
sealed interface MarkdownSpanNode

/**
 * Describes a Markdown paragraph. A paragraph is a collection of spans that make up a block of text.
 *
 * See [Paragraphs](https://commonmark.org/help/tutorial/03-paragraphs.html) for a how-to, or
 * [MarkdownSpanNode] for possible span types.
 *
 * @property children The list of [MarkdownSpanNode]s this paragraph contains.
 */
data class MarkdownParagraph(
    val children: List<MarkdownSpanNode>
) : MarkdownNode

/**
 * Describes a Markdown block quote. A block quote is a fully-formatted Markdown section that is
 * visually distinct from the rest of the document, usually used to indicate a quote.
 *
 * See [Blockquotes](https://commonmark.org/help/tutorial/05-blockquotes.html) for a how-to.
 *
 * @property children The list of [MarkdownNode]s contained within the block quote.
 */
data class MarkdownBlockQuote(
    val children: List<MarkdownNode>
) : MarkdownNode

/**
 * Represents a Markdown rule. A rule is a horizontal line spanning the width of the document,
 * usually used to separate content.
 */
data object MarkdownRule : MarkdownNode

/**
 * Represents whitespace in a Markdown Span.
 */
data object MarkdownWhitespace : MarkdownSpanNode

/**
 * Represents a line end in a Markdown Span.
 */
data object MarkdownEol: MarkdownSpanNode

/**
 * Describes Markdown code block. A code block is a visually distinct section of non-formattable
 * text, usually used to display code.
 *
 * See [Code](https://commonmark.org/help/tutorial/09-code.html) for a how-to.
 *
 * @property code The contents of the code block.
 * @property language The language the code is written in, if specified.
 */
data class MarkdownCodeBlock(
    val code: String,
    val language: String?
): MarkdownNode

/**
 * Describes a Markdown heading. A heading is a line of stylized text, typically larger than the
 * main file content, used to denote the start of a section.
 *
 * See [Headings](https://commonmark.org/help/tutorial/04-headings.html) for a how-to.
 *
 * @property children A list of [MarkdownSpanNode]s that are contained in this heading.
 * @property size The [Size] of the heading.
 */
data class MarkdownHeading(
    val children: List<MarkdownSpanNode>,
    val size: Size,
) : MarkdownNode {

    /**
     * Represents a Markdown heading size. [Headline1] is the largest size, while [Headline6] is the
     * smallest.
     */
    enum class Size {
        Headline1,
        Headline2,
        Headline3,
        Headline4,
        Headline5,
        Headline6,
    }
}

/**
 * Describes a Markdown image. An image is an inline rich media preview.
 *
 * See [Images](https://commonmark.org/help/tutorial/08-images.html) for a how-to.
 *
 * @property imageUrl The URL used to load the image. This may be relative to the URL of the
 * Markdown document.
 * @property contentDescription The content description of the image. This is used to describe the
 * image.
 * @property titleText Optional accompanying text for the image. This can be used to provide
 * additional context.
 */
data class MarkdownImage(
    val imageUrl: String,
    val contentDescription: String,
    val titleText: String?,
) : MarkdownSpanNode

/**
 * Describes a Markdown link. A link is a clickable text element that navigates the user to the
 * specified URL.
 *
 * See [Links](https://commonmark.org/help/tutorial/07-links.html) for a how-to.
 *
 * @property displayText The [MarkdownSpanNode]s that are displayed to the user representing this link.
 * @property titleText Optional accompanying text for the link. This can be used to provide
 * additional context.
 * @property url The link that users should be navigated to upon clicking.
 */
data class MarkdownLink(
    val displayText: List<MarkdownSpanNode>,
    val titleText: String?,
    val url: String,
) : MarkdownSpanNode

/**
 * Describes a Markdown text. A text is a span element that can be displayed with various emphasis.
 *
 * See [Emphasis](https://commonmark.org/help/tutorial/02-emphasis.html) for a how-to.
 *
 * @property text The content of the text span.
 * @property isBold Whether the text span is bold.
 * @property isItalics Whether the text span is italicized.
 * @property isStrikethrough Whether the text span is stricken through.
 */
data class MarkdownText(
    val text: String,
    val isBold: Boolean,
    val isItalics: Boolean,
    val isStrikethrough: Boolean,
) : MarkdownSpanNode

/**
 * Describes a Markdown code span. A code span is a text element that is visually distinct from the
 * surrounding content, and is displayed in a monospace font.
 *
 * See [Code](https://commonmark.org/help/tutorial/09-code.html) for a how-to.
 *
 * @property text The text contained within the code span.
 */
data class MarkdownCodeSpan(
    val text: String,
): MarkdownSpanNode

/**
 * Describes a Markdown table. A table is a grid of Markdown paragraphs, grouped into columns.
 *
 * @property columns The list of [Column]s contained in this table.
 */
data class MarkdownTable(
    val columns: List<Column>
) : MarkdownNode {

    /**
     * Describes a single column within a Markdown table. A column contains a header, and a series
     * of data cells to be displayed under said header.
     *
     * @property header The [MarkdownParagraph] header content.
     * @property cells The list of [MarkdownParagraph]s cell contents.
     * @property alignment The text alignment for this column.
     */
    data class Column(
        val header: MarkdownParagraph,
        val cells: List<MarkdownParagraph>,
        val alignment: Alignment
    )

    /**
     * Represents all possible text alignments for table cell text.
     */
    enum class Alignment {
        LEFT,
        CENTER,
        RIGHT
    }
}

/**
 * Describes a Markdown ordered list. An ordered list is a list of formatted text, displayed as a
 * numbered (ordered) list.
 *
 * See [Lists](https://commonmark.org/help/tutorial/06-lists.html) for a how-to.
 *
 * @property listItems A list of [MarkdownNode]s this list contains.
 */
data class MarkdownOrderedList(
    val listItems: List<MarkdownListItem>
) : MarkdownNode

/**
 * Describes a Markdown unordered list. An unordered list is a list of formatted text, displayed as
 * a bulleted (unordered) list.
 *
 * See [Lists](https://commonmark.org/help/tutorial/06-lists.html) for a how-to.
 *
 * @property listItems A list of [MarkdownNode]s this list contains.
 */
data class MarkdownUnorderedList(
    val listItems: List<MarkdownListItem>
) : MarkdownNode

/**
 * Describes a single item contained within a Markdown list. List items can contain a number of
 * sub-items, for example it could contain one paragraph followed by one (nested) list.
 *
 * @property content A list of [MarkdownNode]s that this list item contains.
 */
data class MarkdownListItem(
    val content: List<MarkdownNode>
)

/**
 * Describes a block of HTML-formatted text contained within a Markdown document.
 *
 * @property text The HTML-formatted text that makes up the block.
 */
data class MarkdownHtmlBlock(
    val text: String
) : MarkdownNode
