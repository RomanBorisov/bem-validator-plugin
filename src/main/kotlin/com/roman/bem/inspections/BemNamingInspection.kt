package com.roman.bem.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.Nls

class BemNamingInspection : BemBaseInspection() {
    @Nls
    override fun getDisplayName(): String = "BEM Naming"

    @Nls
    override fun getGroupDisplayName(): String = "BEM"

    override fun getShortName(): String = "BemNamingInspection"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is XmlTag) {
                    checkNaming(element, holder)
                }
            }
        }
    }

    private fun checkNaming(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        
        for (cls in getClasses(classValue)) {
            // Check for invalid characters
            if (!cls.matches(Regex("^[a-z0-9][a-z0-9_-]*$"))) {
                holder.registerProblem(
                    classAttr,
                    "Class name '$cls' contains invalid characters. Use only lowercase letters, numbers, hyphens and underscores."
                )
                continue
            }
            
            // Check for multiple consecutive underscores
            if (cls.contains("___")) {
                holder.registerProblem(
                    classAttr,
                    "Class name '$cls' contains multiple consecutive underscores."
                )
                continue
            }
            
            // Check for invalid element naming
            if (cls.contains("__")) {
                val parts = cls.split("__")
                if (parts.size > 2) {
                    holder.registerProblem(
                        classAttr,
                        "Class name '$cls' has invalid element naming. Use only one '__' separator."
                    )
                }
            }
            
            // Check for invalid modifier naming
            if (cls.contains("--")) {
                val parts = cls.split("--")
                if (parts.size > 2) {
                    holder.registerProblem(
                        classAttr,
                        "Class name '$cls' has invalid modifier naming. Use only one '--' separator."
                    )
                }
            }
        }
    }
} 