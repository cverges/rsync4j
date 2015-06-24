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
 * com.github.cverges.rsync.Rsync} to filter the file
 * selection criteria based on patterned rules.  It is a parent
 * container for {@link IncludeFilter} and {@link ExcludeFilter}
 * mechanisms.
 *
 * @author Chris Verges
 * @since 1.0
 */
public abstract class Filter implements RsyncOption {
    /**
     * The pattern on which to filter.  The pattern may <b>not</b> have
     * single quotes unless those quotes are properly escaped.
     */
    private String pattern;

    /**
     * Creates a <code>Filter</code> instance with the given pattern.
     *
     * @param pattern the pattern to use when filtering
     */
    public Filter(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Overridden by the concrete subclasses to provide the
     * <code>rsync</code> command line prefix needed, such as
     * <code>--include</code> or <code>--exclude</code>.
     *
     * @return the <code>rsync</code> command line prefix for the filter
     *         rule
     */
    protected abstract String filterType();

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add(filterType() + "='" + pattern + "'");
    }

    /**
     * Returns the pattern.
     *
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }
}

// vim: set ts=4 expandtab:
