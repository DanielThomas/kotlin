/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.JetElement
import org.jetbrains.kotlin.idea.JetBundle
import org.jetbrains.kotlin.psi.psiUtil.getParentOfTypesAndPredicate
import com.intellij.codeInsight.intention.IntentionAction

public abstract class JetSelfTargetingIntention<T: JetElement>(
        public val elementType: Class<T>,
        private var text: String,
        private val familyName: String)
: IntentionAction {
    deprecated("Use primary constructor, no need to use i18n")
    public constructor(key: String, elementType: Class<T>) : this(elementType, JetBundle.message(key), JetBundle.message(key + ".family")) {
    }

    protected fun setText(text: String) {
        this.text = text
    }

    override fun getText() = text
    override fun getFamilyName() = familyName

    public abstract fun isApplicableTo(element: T): Boolean
    public open fun isApplicableTo(element: T, editor: Editor): Boolean = isApplicableTo(element)
    public abstract fun applyTo(element: T, editor: Editor)

    protected fun getTarget(editor: Editor, file: PsiFile): T? {
        val offset = editor.getCaretModel().getOffset()
        return file.findElementAt(offset)?.getParentOfTypesAndPredicate(false, elementType) { element -> isApplicableTo(element, editor) }
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile)
            = getTarget(editor, file) != null

    override fun invoke(project: Project, editor: Editor, file: PsiFile): Unit {
        val target = getTarget(editor, file) ?: error("Intention is not applicable")
        applyTo(target, editor)
    }

    override fun startInWriteAction(): Boolean = true

    override fun toString(): String = getText()
}
