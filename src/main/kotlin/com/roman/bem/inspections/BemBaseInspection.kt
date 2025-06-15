package com.roman.bem.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.psi.xml.XmlAttribute

abstract class BemBaseInspection : LocalInspectionTool() {
    protected fun getClasses(classValue: String): List<String> {
        return classValue.split(" ").filter { it.isNotEmpty() }
    }
} 