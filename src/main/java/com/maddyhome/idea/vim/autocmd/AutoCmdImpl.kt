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

  private val eventHandlers: MutableMap<AutoCmdEvent, MutableList<String>> = mutableMapOf()

  override fun notifyModeChanged(
    oldMode: Mode,
    newMode: Mode,
  ) {
    if (oldMode != Mode.INSERT && newMode == Mode.INSERT) {
      handleEvent(AutoCmdEvent.InsertEnter)
    }
  }

  override fun registerEventCommand(command: String, event: AutoCmdEvent) {
   eventHandlers.getOrPut(event) { mutableListOf() }.add(command)
  }

  fun handleEvent(event: AutoCmdEvent) {
    eventHandlers[event]?.forEach { executeCommand(it) }
  }

  private fun executeCommand(command: String) {
    val editor = injector.editorGroup.getFocusedEditor() ?: return
    val context = injector.executionContextManager.getEditorExecutionContext(editor)
    injector.vimscriptExecutor.execute(command, editor, context, skipHistory = true)
  }
}