/*
 * Copyright (c) 2015 Sentient Energy, Inc.
 * Copyright (c) 2015 Chris Verges
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.github.cverges.rsync;

import expect4j.BufferChangeLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class RawOutputListener implements BufferChangeLogger {
    private static final Logger logger = LogManager.getLogger(RawOutputListener.class);

    public static final String eolTrimRegex = "\\n$";

    @Override
    public void bufferChanged(char[] newData, int numChars) {
        if (numChars == 0) {
            return;
        }

        String output = new String(newData, 0, numChars);
        System.out.println(output.replaceAll(eolTrimRegex, ""));
    }
}
