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
 * This class represents the statistics details reported by
 * <code>rsync</code> at the end of a file transfer.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class TransferStats {
    private String numFiles;
    private String numFilesTransferred;
    private String totalFileSize;
    private String totalFileSizeTransferred;
    private String literalData;
    private String matchedData;
    private String fileListSize;
    private String fileListGenerationTime;
    private String fileListTransferTime;
    private String totalBytesSent;
    private String totalBytesReceived;

    public TransferStats(String numFiles, String numFilesTransferred, String totalFileSize, String totalFileSizeTransferred, String literalData, String matchedData, String fileListSize, String fileListGenerationTime, String fileListTransferTime, String totalBytesSent, String totalBytesReceived) {
        this.numFiles = numFiles;
        this.numFilesTransferred = numFilesTransferred;
        this.totalFileSize = totalFileSize;
        this.totalFileSizeTransferred = totalFileSizeTransferred;
        this.literalData = literalData;
        this.matchedData = matchedData;
        this.fileListSize = fileListSize;
        this.fileListGenerationTime = fileListGenerationTime;
        this.fileListTransferTime = fileListTransferTime;
        this.totalBytesSent = totalBytesSent;
        this.totalBytesReceived = totalBytesReceived;
    }

    public String getNumFiles() {
        return numFiles;
    }

    public String getNumFilesTransferred() {
        return numFilesTransferred;
    }

    public String getTotalFileSize() {
        return totalFileSize;
    }

    public String getTotalFileSizeTransferred() {
        return totalFileSizeTransferred;
    }

    public String getLiteralData() {
        return literalData;
    }

    public String getMatchedData() {
        return matchedData;
    }

    public String getFileListSize() {
        return fileListSize;
    }

    public String getFileListGenerationTime() {
        return fileListGenerationTime;
    }

    public String getFileListTransferTime() {
        return fileListTransferTime;
    }

    public String getTotalBytesSent() {
        return totalBytesSent;
    }

    public String getTotalBytesReceived() {
        return totalBytesReceived;
    }
}
