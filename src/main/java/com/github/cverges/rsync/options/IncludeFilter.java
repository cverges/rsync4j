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

/**
 * This class modifies the behavior of {@link
 * com.github.cverges.rsync.Rsync} to include a file
 * based on patterned rules.  It is equivalent to the
 * <code>--exclude=PATTERN</code> argument for <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class IncludeFilter extends Filter {
    /**
     * Creates an <code>IncludeFilter</code> instance with the given
     * pattern.
     *
     * @param pattern the pattern to use when filtering
     */
    public IncludeFilter(String pattern) {
        super(pattern);
    }

    /**
     * Returns <code>--include</code>.
     *
     * @return <code>--include</code>
     */
    protected String filterType() {
        return "--include";
    }
}

// vim: set ts=4 expandtab:
