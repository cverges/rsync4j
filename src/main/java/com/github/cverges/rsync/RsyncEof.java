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

/**
 * This class represents an end-of-file (EOF) reported by
 * <code>rsync</code> during a file transfer.  It may not be an {@link
 * RsyncError}, per se, but may be a normal part of the transfer
 * process.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class RsyncEof extends RsyncError {
    public RsyncEof(String buffer) {
        super(buffer);
    }
}
