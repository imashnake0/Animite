# Markdown guide
Press understands standard markdown syntaxes, including: **bold**, *italic*, ~~strikethrough~~, and many more:

### Code blocks
```
fun helloWorld() {
  println("""
    Code blocks are wrapped inside 
    three ticks.
  """)
}
``` 

### Headings
Headings start with 1-6 `#` characters at the start of the line. 

```
# Heading 1
## Heading 2
### Heading 3
#### Heading 4
##### Heading 5
###### Heading 6
```

### Links
Links use a set of square brackets (`[]`) for describing the link text, followed by regular parentheses (`()`) containing the URL. 

[Rick and Morty](https://www.imdb.com/title/tt2861424/).

### Lists
Press supports ordered (numbered) and unordered (bulleted) lists. Unordered lists use asterisks, pluses, and hyphens — interchangeably — as list markers:

- National Treasure
+ Ghost Rider
* Face/Off

Ordered lists use numbers followed by periods:

1. The Last of Us
2. Death Stranding
3. Cyberpunk 2077

### Thematic breaks
Lines starting with three asterisks (`*`), hyphens (`-`) or underscores (`_`) are rendered as horizontal rules, a.k.a. “thematic breaks”. 

---

### Quotes
> A paragraph starting with a `>` are rendered as a quote. 