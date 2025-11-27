package com.imashnake.animite.api.anilist.sanitize.media

import com.imashnake.animite.api.anilist.fragment.CharacterSmall
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaTest {
    // region character
    @Test
    fun `character is transformed correctly`() {
        val actual = Media.Character(getCharacterSmall())
        val expected = getMediaCharacter()

        assertEquals(expected, actual)
    }

    @Test
    fun `character alternative names is empty if there are none`() {
        val actual = Media.Character(getCharacterSmall(alternative = emptyList()))

        assertTrue(actual.alternativeNames.isEmpty())
    }

    @Test
    fun `character dob is null if year and month are null`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = null,
                month = null,
                day = 23
            )
        ))

        assertNull(actual.dob)
    }

    @Test
    fun `character dob is year if only month is null`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = 2025,
                month = null,
                day = 23
            )
        ))

        assertEquals("2025", actual.dob)
    }

    @Test
    fun `character dob if year is null`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = null,
                month = 11,
                day = 23
            )
        ))

        assertEquals("Nov 23", actual.dob)
    }

    @Test
    fun `character dob if day is null`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = 2025,
                month = 11,
                day = null
            )
        ))

        assertEquals("Nov 2025", actual.dob)
    }

    @Test
    fun `character dob if year and day are null`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = null,
                month = 11,
                day = null
            )
        ))

        assertEquals("Nov", actual.dob)
    }

    @Test
    fun `character dob`() {
        val actual = Media.Character(getCharacterSmall(
            dateOfBirth = CharacterSmall.DateOfBirth(
                year = 2025,
                month = 11,
                day = 23
            )
        ))

        assertEquals("Nov 23, 2025", actual.dob)
    }

    @Test
    fun `character favourites is null`() {
        val actual = Media.Character(getCharacterSmall(
            favourites = null
        ))

        assertNull(actual.favourites)
    }

    @Test
    fun `character favourites does not have decimal for whole thousands`() {
        val actual = Media.Character(getCharacterSmall(
            favourites = 15000
        ))

        assertEquals("15k", actual.favourites)
    }

    @Test
    fun `character favourites is not a whole thousand`() {
        val actual = Media.Character(getCharacterSmall(
            favourites = 123456
        ))

        assertEquals("123.5k", actual.favourites)
    }

    @Test
    fun `character description when age is null`() {
        val actual = Media.Character(getCharacterSmall(
            age = null,
        ))

        assertEquals("<b>Gender:</b> male<br>description", actual.description)
    }

    @Test
    fun `character description when gender is null`() {
        val actual = Media.Character(getCharacterSmall(
            gender = null,
        ))

        assertEquals("<b>Age:</b> 50<br>description", actual.description)
    }

    @Test
    fun `character description when age and gender are null`() {
        val actual = Media.Character(getCharacterSmall(
            age = null,
            gender = null,
        ))

        assertEquals("description", actual.description)
    }

    private fun getCharacterSmall(
        alternative: List<String?>? = listOf("name1", "name2"),
        dateOfBirth: CharacterSmall.DateOfBirth = CharacterSmall.DateOfBirth(
            year = 2025,
            month = 11,
            day = 23
        ),
        favourites: Int? = 150,
        age: String? = "50",
        gender: String? = "male"
    ) = CharacterSmall(
        id = 12345,
        image = CharacterSmall.Image(
            large = "image"
        ),
        name = CharacterSmall.Name(
            full = "fullName",
            alternative = alternative
        ),
        description = "description",
        gender = gender,
        dateOfBirth = dateOfBirth,
        age = age,
        favourites = favourites
    )

    private fun getMediaCharacter() = Media.Character(
        id = 12345,
        image = "image",
        name = "fullName",
        alternativeNames = "name1, name2",
        description = "<b>Age:</b> 50<br><b>Gender:</b> male<br>description",
        dob = "Nov 23, 2025",
        favourites = "150"
    )
    // endregion
}
