package com.roman.bem.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.psi.xml.XmlAttribute
import org.jetbrains.annotations.Nls
import java.io.InputStreamReader

abstract class BemBaseInspection : LocalInspectionTool() {
    protected fun getClasses(classValue: String): List<String> {
        return classValue.split(" ").filter { it.isNotEmpty() }
    }

    @Nls
    override fun getStaticDescription(): String? {
        val resourcePath = "/inspectionDescriptions/${this.shortName}.html"
        return javaClass.getResourceAsStream(resourcePath)?.use { stream ->
            InputStreamReader(stream).use { reader ->
                reader.readText()
            }
        }
    }
} 