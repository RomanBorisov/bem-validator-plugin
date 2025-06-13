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
                    checkBemElementHasBlockParent(element, holder)
                    checkModifierHasBaseClass(element, holder)
                    checkNoElementOrModifierWithoutBlock(element, holder)
                    checkNoDoubleSeparators(element, holder)
                    checkBlockNaming(element, holder)
                    checkElementNestingDepth(element, holder)
                }
            }
        }
    }

    private fun checkBemElementHasBlockParent(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class")
        val classValue = classAttr?.value ?: return
        val bemElementRegex = Regex("([a-zA-Z0-9_-]+)__([a-zA-Z0-9_-]+)")
        for (cls in classValue.split(" ")) {
            val match = bemElementRegex.matchEntire(cls) ?: continue
            val block = match.groupValues[1]
            if (!hasBlockParent(element, block)) {
                holder.registerProblem(
                    classAttr,
                    "Element '$cls' is used outside of block '$block'."
                )
            }
        }
    }

    private fun checkModifierHasBaseClass(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        val modifierRegex = Regex("([a-zA-Z0-9_-]+)(__(?:[a-zA-Z0-9_-]+))?--([a-zA-Z0-9_-]+)")
        val classes = classValue.split(" ")
        for (cls in classes) {
            val match = modifierRegex.matchEntire(cls) ?: continue
            val block = match.groupValues[1]
            val elementPart = match.groupValues[2] // может быть пустым
            val baseClass = if (elementPart.isNotEmpty()) block + elementPart else block
            if (baseClass.isEmpty()) continue
            if (baseClass !in classes) {
                holder.registerProblem(
                    classAttr,
                    "Modifier '$cls' is used without its base class '$baseClass'."
                )
            }
        }
    }

    private fun hasBlockParent (tag: XmlTag, block: String): Boolean {
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

    private fun checkNoElementOrModifierWithoutBlock(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        val classes = classValue.split(" ")
        for (cls in classes) {
            if (cls.startsWith("__") || cls.startsWith("--")) {
                holder.registerProblem(
                    classAttr,
                    "Class '$cls' starts with a separator without a block name."
                )
            }
        }
    }

    private fun checkNoDoubleSeparators(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        val classes = classValue.split(" ")
        for (cls in classes) {
            if (cls.contains("___") || cls.contains("---")) {
                holder.registerProblem(
                    classAttr,
                    "Class '$cls' contains double separators."
                )
            }
        }
    }

    private fun checkBlockNaming(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        val classes = classValue.split(" ")
        
        // Регулярное выражение для проверки имени блока
        // Разрешает только строчные буквы, цифры и дефисы
        val blockNameRegex = Regex("^[a-z0-9-]+$")
        
        for (cls in classes) {
            // Проверяем только имена блоков (без элементов и модификаторов)
            if (!cls.contains("__") && !cls.contains("--")) {
                if (!blockNameRegex.matches(cls)) {
                    holder.registerProblem(
                        classAttr,
                        "Block name '$cls' should be lowercase and use only letters, numbers and hyphens."
                    )
                }
            }
        }
    }

    private fun checkElementNestingDepth(element: XmlTag, holder: ProblemsHolder) {
        val classAttr = element.getAttribute("class") ?: return
        val classValue = classAttr.value ?: return
        val classes = classValue.split(" ")
        
        for (cls in classes) {
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
