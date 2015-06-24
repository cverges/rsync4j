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
 * com.github.cverges.rsync.Rsync} to use a defined block
 * size when performing checksum comparisons.  By default,
 * <code>rsync</code> selects the most efficient block size based on
 * file size.  This option overrides that behavior and forces a single
 * block size to be used for everything.  It is equivalent to the
 * <code>--block-size=SIZE</code> argument for <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class BlockSize implements RsyncOption {
    /**
     * The size of the checksum block.
     */
    private long size;

    /**
     * Creates a <code>BlockSize</code> instance with the given
     * size.
     *
     * @param size the block size to use
     */
    public BlockSize(long size) {
        this.size = size;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--block-size=" + size);
    }
}

// vim: set ts=4 expandtab:
