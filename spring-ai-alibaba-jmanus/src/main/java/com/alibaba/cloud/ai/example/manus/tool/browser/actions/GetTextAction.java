/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.ai.example.manus.tool.browser.actions;

import com.microsoft.playwright.Page;

import com.alibaba.cloud.ai.example.manus.tool.browser.BrowserUseTool;
import com.alibaba.cloud.ai.example.manus.tool.code.ToolExecuteResult;

public class GetTextAction extends BrowserAction {

	private final static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GetTextAction.class);

	public GetTextAction(BrowserUseTool browserUseTool) {
		super(browserUseTool);
	}

	@Override
	public ToolExecuteResult execute(BrowserRequestVO request) throws Exception {
		Page page = getCurrentPage(); // Get Playwright Page instance
		StringBuilder allText = new StringBuilder();
		for (com.microsoft.playwright.Frame frame : page.frames()) {
			try {
				String text = frame.innerText("body");
				if (text != null && !text.isEmpty()) {
					allText.append(text).append("\\n");
				}
			}
			catch (Exception e) {
				// Ignore frames without body
			}
		}
		String result = allText.toString().trim();

		// Log only first 10 lines for brevity
		String[] lines = result.split("\n");
		String logPreview = lines.length > 10 ? String.join("\n", java.util.Arrays.copyOf(lines, 10)) + "\n... (total "
				+ lines.length + " lines, showing first 10)" : result;
		log.info("get_text all frames body is {}", logPreview);
		log.debug("get_text all frames body is {}", result);
		return new ToolExecuteResult(result);
	}

}
