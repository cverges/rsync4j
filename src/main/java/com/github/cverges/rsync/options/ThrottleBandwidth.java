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
 * com.github.cverges.rsync.Rsync} to throttle the
 * bandwidth used by the connection to no more than the defined amount.
 * It is equivalent to the <code>--bwlimit=KBPS</code> argument for
 * <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class ThrottleBandwidth implements RsyncOption {
    /**
     * The number of kilobytes per second that should be used for the
     * {@link com.github.cverges.rsync.Rsync} transfer.
     */
    private int limit;

    /**
     * Creates a <code>ThrottleBandwidth</code> instance with the given
     * limit.
     *
     * @param limit the maximum transfer rate (in KB/s)
     */
    public ThrottleBandwidth(int limit) {
        this.limit = limit;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--bwlimit=" + limit);
    }
}

// vim: set ts=4 expandtab:
