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
 * com.github.cverges.rsync.Rsync} to terminate if the
 * remote side has gone silent for more than a defined amount of time.
 * It is equivalent to the <code>--timeout=SECS</code> argument for
 * <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class InactivityTimer implements RsyncOption {
    /**
     * The number of seconds that the connection is allowed to go
     * inactive before declaring the connection dead.
     */
    private long timeout;

    /**
     * Creates a <code>InactivityTimer</code> instance with the given
     * timeout.
     *
     * @param timeout the maximum inactivity time allowed (in seconds)
     */
    public InactivityTimer(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--timeout=" + timeout);
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
