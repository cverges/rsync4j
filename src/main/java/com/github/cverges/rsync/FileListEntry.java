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
 * This class represents the beginning of a file transfer as reported by
 * <code>rsync</code>.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class FileListEntry {
    private String permissions;
    private String size;
    private String timestamp;
    private String filename;

    public FileListEntry(String permissions, String size, String yyyymmdd, String hhmmss, String filename) {
        this.permissions = permissions;
        this.size = size;
        this.timestamp = yyyymmdd + " " + hhmmss;
        this.filename = filename;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getSize() {
        return size;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFilename() {
        return filename;
    }
}
