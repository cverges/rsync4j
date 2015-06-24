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
 * com.github.cverges.rsync.Rsync} to remove source files
 * once they have been successfully transferred to the destination.
 * It is equivalent to the <code>--remove-source-files</code> argument
 * for <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class RemoveSourceFiles implements RsyncOption {
    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("--remove-source-files");
    }
}

// vim: set ts=4 expandtab:
