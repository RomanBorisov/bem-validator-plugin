package com.roman.bem.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.Nls

class BemElementBlockInspection : BemBaseInspection() {
    @Nls
    override fun getDisplayName(): String = "BEM Element Block"

    override fun getShortName(): String = "BemElementBlockInspection"

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is XmlTag) {
                    checkElementBlock(element, holder)
                }
            }
        }
    }

    private fun checkElementBlock(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        
        for (cls in getClasses(classValue)) {
            if (cls.contains("__")) {
                val parts = cls.split("__")
                if (parts.size > 1) {
                    val parentBlock = parts[0]
                    val elementName = parts[1]
                    
                    // Check if parent block exists
                    var parentBlockFound = false
                    var currentElement = element.parent
                    
                    while (currentElement != null) {
                        if (currentElement is XmlTag) {
                            val parentClassAttr = currentElement.getAttribute("class")
                            if (parentClassAttr != null) {
                                val parentClasses = getClasses(parentClassAttr.value ?: "")
                                if (parentClasses.contains(parentBlock)) {
                                    parentBlockFound = true
                                    break
                                }
                            }
                        }
                        currentElement = currentElement.parent
                    }
                    
                    if (!parentBlockFound) {
                        holder.registerProblem(
                            classAttr,
                            "Element '$elementName' is not properly nested within its parent block '$parentBlock'"
                        )
                    }
                }
            }
        }
    }
} 