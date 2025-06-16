package ru.bem.inspections

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.roman.bem.inspections.BemElementBlockInspection

class BemHtmlInspectionTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testElementWithoutBlockParent() {
        myFixture.enableInspections(BemElementBlockInspection())
        myFixture.configureByText("test.html", """
            <div class="menu__item<caret>"></div>
        """.trimIndent())
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("Element 'item' is not properly nested within its parent block 'menu'") == true })
    }

    fun testElementWithBlockParent() {
        myFixture.enableInspections(BemElementBlockInspection())
        myFixture.configureByText("test.html", """
            <div class="menu">
              <div class="menu__item"></div>
            </div>
        """.trimIndent())
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("Element 'item' is not properly nested within its parent block 'menu'") == true })
    }
} 