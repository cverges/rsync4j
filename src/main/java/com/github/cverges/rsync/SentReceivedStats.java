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
 * This class represents the statistics summary reported by
 * <code>rsync</code> at the end of a file transfer.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class SentReceivedStats {
    private String bytesSent;
    private String bytesReceived;
    private String transferRate;
    private String totalSize;
    private String speedup;

    public SentReceivedStats(String bytesSent, String bytesReceived, String transferRate, String totalSize, String speedup) {
        this.bytesSent = bytesSent;
        this.bytesReceived = bytesReceived;
        this.transferRate = transferRate;
        this.totalSize = totalSize;
        this.speedup = speedup;
    }

    public String getBytesSent() {
        return bytesSent;
    }

    public String getBytesReceived() {
        return bytesReceived;
    }

    public String getTransferRate() {
        return transferRate;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public String getSpeedup() {
        return speedup;
    }
}
