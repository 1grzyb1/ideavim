/*
 * Copyright 2003-2025 The IdeaVim authors
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE.txt file or at
 * https://opensource.org/licenses/MIT.
 */

package org.jetbrains.plugins.ideavim.autocmd

import com.maddyhome.idea.vim.api.injector
import org.jetbrains.plugins.ideavim.VimTestCase
import org.junit.jupiter.api.Test

class AutoCmdTest :  VimTestCase() {

  @Test
  fun `should execute command on InsertEnter`() {
    configureByText("asdfasd")
    enterCommand("autocmd InsertEnter * echo 23")
    typeText(injector.parser.parseKeys("i"))
    assertExOutput("23")
  }


  @Test
  fun `should do nothing on invalid syntax`() {
    configureByText("asdfasd")
    enterCommand("autocmd InsertEnter  echo 23")
    typeText(injector.parser.parseKeys("i"))
    assertNoExOutput()
  }
}