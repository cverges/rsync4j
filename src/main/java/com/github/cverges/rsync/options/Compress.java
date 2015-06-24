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

package com.github.cverges.rsync.options;

import java.util.List;

/**
 * This class modifies the behavior of {@link
 * com.github.cverges.rsync.Rsync} to compress data
 * before transferring from source to destination.  It is equivalent to
 * the <code>--compress</code> and <code>--compress-level=NUM</code>
 * arguments for <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class Compress implements RsyncOption {
    /**
     * The compression level (from zlib).
     */
    private int level;

    /**
     * Creates a <code>Compress</code> instance with the default
     * compression level.
     */
    public Compress() {
        this(6);
    }

    /**
     * Creates a <code>Compress</code> instance with the given
     * compression level.
     *
     * @param level the zlib compression level (1 thru 9)
     */
    public Compress(int level) {
        this.level = level;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--compress");
        args.add("--compress-level=" + level);
    }
}

// vim: set ts=4 expandtab:
