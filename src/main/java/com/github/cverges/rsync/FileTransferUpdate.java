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
 * This class represents an update of transfer statistics reported by
 * <code>rsync</code> during a file transfer.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class FileTransferUpdate {
    private String bytesTransferred;
    private String percentComplete;
    private String transferRate;
    private String timeLeft;

    public FileTransferUpdate(String bytesTransferred, String percentComplete, String transferRate, String timeLeft) {
        this.bytesTransferred = bytesTransferred;
        this.percentComplete = percentComplete;
        this.transferRate = transferRate;
        this.timeLeft = timeLeft;
    }

    public String getBytesTransferred() {
        return bytesTransferred;
    }

    public String getPercentComplete() {
        return percentComplete;
    }

    public String getTransferRate() {
        return transferRate;
    }

    public String getTimeLeft() {
        return timeLeft;
    }
}
