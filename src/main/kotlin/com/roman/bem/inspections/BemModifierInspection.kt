package com.roman.bem.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.Nls

class BemModifierInspection : BemBaseInspection() {
    @Nls
    override fun getDisplayName(): String = "BEM Modifier"

    override fun getShortName(): String = "BemModifierInspection"

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is XmlTag) {
                    checkModifier(element, holder)
                }
            }
        }
    }

    private fun checkModifier(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        
        for (cls in getClasses(classValue)) {
            if (cls.contains("--")) {
                val parts = cls.split("--")
                if (parts.size > 1) {
                    val baseClass = parts[0]
                    val modifier = parts[1]
                    
                    // Check if base class exists
                    if (!getClasses(classValue).contains(baseClass)) {
                        holder.registerProblem(
                            classAttr,
                            "Modifier '$modifier' is used without its base class '$baseClass'"
                        )
                    }
                }
            }
        }
    }
} 