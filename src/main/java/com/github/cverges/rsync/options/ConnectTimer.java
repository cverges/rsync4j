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
 * com.github.cverges.rsync.Rsync} to terminate if a
 * connection to the remote side cannot be made within a defined amount
 * of time.  It is equivalent to the <code>--contimeout=SECS</code>
 * argument for <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class ConnectTimer implements RsyncOption {
    /**
     * The number of seconds that we should allow for a connection to
     * establish.
     */
    private long timeout;

    /**
     * Creates a <code>ConnectTimer</code> instance with the given
     * timeout.
     *
     * @param timeout the maximum time allowed to attempt a connection
     *                (in seconds)
     */
    public ConnectTimer(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--contimeout=" + timeout);
    }

    /**
     * Returns the timeout.
     *
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }
}

// vim: set ts=4 expandtab:
