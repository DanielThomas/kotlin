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

package org.jetbrains.kotlin.idea.refactoring.rename

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.JetSimpleNameExpression
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.intentions.ReplaceItWithExplicitFunctionLiteralParamIntention
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.refactoring.rename.inplace.VariableInplaceRenameHandler
import org.jetbrains.kotlin.idea.util.application.executeWriteCommand

public class RenameKotlinImplicitLambdaParameter: VariableInplaceRenameHandler() {
    override fun isAvailable(element: PsiElement?, editor: Editor, file: PsiFile): Boolean {
        val simpleNameExpression = PsiTreeUtil.findElementOfClassAtOffset(
                file, editor.getCaretModel().getOffset(), javaClass<JetSimpleNameExpression>(), false)

        return simpleNameExpression != null
                && ReplaceItWithExplicitFunctionLiteralParamIntention.isAutoCreatedIt(simpleNameExpression)
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile, dataContext: DataContext?) {
        val intention = ReplaceItWithExplicitFunctionLiteralParamIntention()
        project.executeWriteCommand("Convert 'it' to explicit lambda parameter") {
            intention.invoke(project, editor, file)
        }
    }

    override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
        // Do nothing: this method is called not from editor
    }
}
