package ru.bem.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NotNull

class BemHtmlInspection : LocalInspectionTool() {
    @Nls
    override fun getDisplayName(): String = "BEM HTML Inspector"

    @Nls
    override fun getGroupDisplayName(): String = "BEM"

    override fun getShortName(): String = "BemHtmlInspection"

    override fun isEnabledByDefault(): Boolean = true

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean
    ): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: com.intellij.psi.PsiElement) {
                if (element is XmlTag) {
                    val classAttr = element.getAttribute("class")
                    if (classAttr != null) {
                        holder.registerProblem(
                            classAttr,
                            "Test error: inspection is connected!"
                        )
                    }
                }
            }
        }
    }

    private fun hasBlockParent(tag: XmlTag, block: String): Boolean {
        var parent = PsiTreeUtil.getParentOfType(tag, XmlTag::class.java)
        while (parent != null) {
            val parentClass = parent.getAttribute("class")?.value ?: ""
            if (parentClass.split(" ").any { it == block }) {
                return true
            }
            parent = PsiTreeUtil.getParentOfType(parent, XmlTag::class.java)
        }
        return false
    }
} 