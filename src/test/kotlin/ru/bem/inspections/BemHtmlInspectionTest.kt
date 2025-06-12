package ru.bem.inspections

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class BemHtmlInspectionTest : BasePlatformTestCase() {
    fun testElementWithoutBlockParent() {
        myFixture.enableInspections(BemHtmlInspection())
        myFixture.configureByText("test.html", """
            <div class="menu__item<caret>"></div>
        """)
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("используется вне блока") == true })
    }

    fun testElementWithBlockParent() {
        myFixture.enableInspections(BemHtmlInspection())
        myFixture.configureByText("test.html", """
            <div class="menu">
              <div class="menu__item"></div>
            </div>
        """)
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("используется вне блока") == true })
    }
} 