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
 * This class provides the interfaces needed to build various decorator
 * options for {@link com.github.cverges.rsync.Rsync}.
 *
 * <p><b>Typical Usage</b>
 *
 * <pre>TODO: Code Example Here</pre>
 *
 * @author Chris Verges
 * @since 1.0
 */
public interface RsyncOption {
    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args);
}

// vim: set ts=4 expandtab:
