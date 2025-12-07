/*
 * Copyright 2003-2025 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package com.maddyhome.idea.vim.autocmd

import com.maddyhome.idea.vim.api.AutoCmdService
import com.maddyhome.idea.vim.api.injector
import com.maddyhome.idea.vim.state.mode.Mode

class AutoCmdImpl : AutoCmdService {

  override fun notifyModeChanged(
    oldMode: Mode,
    newMode: Mode,
  ) {
   if (oldMode != Mode.INSERT && newMode == Mode.INSERT) {
     val editor = injector.editorGroup.getFocusedEditor() ?: return
     val context = injector.executionContextManager.getEditorExecutionContext(editor)
     injector.vimscriptExecutor.execute("echo 23", editor, context, skipHistory = true)
   }
  }
}