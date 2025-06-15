package com.roman.bem.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.Nls

class BemNestingInspection : BemBaseInspection() {
    @Nls
    override fun getDisplayName(): String = "BEM Nesting"

    override fun getShortName(): String = "BemNestingInspection"

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is XmlTag) {
                    checkElementNestingDepth(element, holder)
                }
            }
        }
    }

    private fun checkElementNestingDepth(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        
        for (cls in getClasses(classValue)) {
            if (cls.contains("__")) {
                val parts = cls.split("__")
                if (parts.size > 2) {
                    holder.registerProblem(
                        classAttr,
                        "Too deep element nesting in class '$cls'. Maximum nesting level is 1."
                    )
                }
            }
        }
    }
} 