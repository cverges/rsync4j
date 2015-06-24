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

import com.google.common.base.Joiner;
import com.github.cverges.rsync.options.RsyncOption;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class provides a Java interface to the standard Linux/Unix
 * <code>rsync</code> command.  Rsync transfers files between systems
 * using a differential compression algorithm to minimize the transfer
 * traffic.
 *
 * <p>There is no native rsync implementation for Java at this time, so
 * we are leveraging Expect4J to perform scripted control of the
 * <code>rsync</code> utility in a spawned {@link java.lang.Process}
 * context.
 *
 * <p><code>Rsync</code> supports multiple decorators to tweak the
 * behavior of the system.  For example, an encrypted rsync tunnel can
 * be created using SSH by applying the {@link
 * com.github.cverges.rsync.options.SshTunnel} decorator.
 *
 * <p><b>Typical Usage</b>
 *
 * <pre>import com.github.cverges.rsync.Rsync;
 * import com.github.cverges.rsync.options.*;
 * import java.util.Observer;
 *
 * Rsync rsync = new Rsync();
 *
 * Observer myCustomObserver;
 * rsync.addObserver(myCustomerObserver);
 *
 * boolean success = false;
 * try {
 *     success = rsync.addOption(new NoMotdBanner())
 *                    .addOption(new ThrottleBandwidth(5))
 *                    .addOption(new SshTunnel("username", "~/.ssh/id_rsa", 2222))
 *                    .setSource(new URI("rsync://dns-or-ip:port/module/directory/"))
 *                    .setDestination(new URI("file:///data/archive"))
 *                    .transfer();
 * } catch (Exception e) {
 *     logger.warn("Caught exception: " + e);
 * }</pre>
 *
 * @author Chris Verges
 * @see <a href="http://github.com/cverges/expect4j">Expect4J</a>
 * @since 1.0
 */
public class Rsync extends Observable {
    /**
     * Interface to the Java 2 platform's core logging facilities.
     */
    private static final Logger logger = LogManager.getLogger(Rsync.class);

    /**
     * The source directory or file(s) to transfer, specified as a
     * Uniform Resource Identifier (URI).  This should be a
     * fully-defined URI, such as the following:
     *
     * <pre>rsync://127.0.0.1/module/directory/
     * rsync://10.1.2.3/module/directory/file-pattern.*
     * rsync://172.16.0.1/module/directory/single-file
     * file:///path/to/my/file.txt</pre>
     */
    private URI source;

    /**
     * The destination directory or file, specified as a Uniform
     * Resource Identifier (URI).  This should be a fully-defined URI,
     * such as the following:
     *
     * <pre>rsync://127.0.0.1/module/directory/
     * rsync://172.16.0.1/module/directory/single-file
     * file:///path/to/my/file.txt</pre>
     *
     * Note that a destination directory must be used if the source
     * defines a directory or multiple files.
     */
    private URI destination;

    /**
     * A list of {@link RsyncOption}s that can be used to tweak the
     * behavior of <code>Rsync</code>.  Examples of these options
     * include ignoring the message of the day (MOTD) banner, throttling
     * the connection bandwidth, and including/excluding sources based
     * on pattern matching
     */
    protected List<RsyncOption> options;

    /**
     * Sets the source for the transfer.
     *
     * @param source the source as specified by a fully qualified URI
     * @return a reference to the <code>Rsync</code> instance to support
     *         chaining
     */
    public Rsync setSource(URI source) {
        this.source = source;
        return this;
    }

    /**
     * Sets the destination for the transfer.
     *
     * @param destination the destination as specified by a fully qualified URI
     * @return a reference to the <code>Rsync</code> instance to support
     *         chaining
     */
    public Rsync setDestination(URI destination) {
        this.destination = destination;
        return this;
    }

    /**
     * Adds a {@link RsyncOption} to the options list.
     *
     * @param option the new <code>RsyncOption</code> to add
     * @return a reference to the <code>Rsync</code> instance to support
     *         chaining
     */
    public Rsync addOption(RsyncOption option) {
        if (options == null)
            options = new ArrayList<RsyncOption>();
        options.add(option);
        return this;
    }

    /**
     * Performs a file transfer from {@link Rsync#source} to {@link
     * Rsync#destination}.  All parameters of the transfer (e.g. source,
     * destination, and options) must be setup prior to calling this
     * function.
     * <p>
     * The call to <code>transfer()</code> is blocking, but runtime
     * status in the interim can be obtained by registering an {@link
     * java.util.Observer}.  Note that the <code>Observer</code> must
     * quickly process any notifications since these occur in the
     * context of the <code>transfer()</code> and can adversely impact
     * <code>Expect4j</code> timeouts.
     *
     * @return <code>true</code> if successful, <code>false</code> on an
     *         error
     * @throws IllegalStateException if the <code>Rsync</code> object is
     *                               not yet setup for a transfer
     */
    public boolean transfer() throws IllegalStateException {
        // Sanity check first
        if (this.source == null) {
            String msg = "Unable to start rsync transfer: missing source";
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }

        if (this.destination == null) {
            String msg = "Unable to start rsync transfer: missing destination";
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }

        if (this.countObservers() == 0) {
            logger.debug("No rsync observers registered, so no progress will be reported during the transfer");
        }

        // Build up the rsync command itself
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("rsync");                       // start with the program name
        cmdArgs.add("-Pavch");                      // basic parameters that we require for parsing
        cmdArgs.add("--out-format");
        cmdArgs.add("\"file: %f\"");
        applyOptions(cmdArgs);
        cmdArgs.add(uriToRsyncArg(source));
        cmdArgs.add(uriToRsyncArg(destination));

        // Offload the heavy lifting to our helper function
        return parser(cmdArgs, null);
    }

    /**
     * Strips any <code>file://</code> prefixes from the URI so that it
     * is compatible with <code>rsync</code>.
     *
     * @param input the URI to evaluate
     * @return the URI as a string, stripped if needed
     */
    private String uriToRsyncArg(URI input) {
        return input.toString().replaceFirst("^file://", "");
    }

    /**
     * Gathers a list of files from {@link Rsync#source}.  The source
     * URI must be setup prior to calling this function.
     * <p>
     * The call to <code>list()</code> is blocking, but
     * runtime status in the interim can be obtained by registering an
     * {@link java.util.Observer}.  Note that the <code>Observer</code>
     * must quickly process any notifications since these occur in the
     * context of the <code>transfer()</code> and can adversely impact
     * <code>Expect4j</code> timeouts.
     *
     * @param fileList a list that contains the results of the operation
     * @return <code>true</code> if successful, <code>false</code> on an
     *         error
     * @throws IllegalStateException if the <code>Rsync</code> object is
     *                               not yet setup for a transfer
     */
    public boolean list(List<FileListEntry> fileList) throws IllegalStateException {
        // Sanity check first
        if (this.source == null) {
            String msg = "Unable to start rsync operation: missing source";
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }

        if (fileList == null) {
            String msg = "Unable to start rsync operation: cannot store file list into 'null' container";
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }

        if (this.countObservers() == 0) {
            logger.debug("No rsync observers registered, so no progress will be reported during the transfer");
        }

        // Build up the rsync command itself
        List<String> cmdArgs = new ArrayList<String>();
        cmdArgs.add("rsync");                       // start with the program name
        cmdArgs.add("-Pavch");                      // basic parameters that we require for parsing
        cmdArgs.add("--out-format");
        cmdArgs.add("\"file: %f\"");
        cmdArgs.add("--list-only");
        applyOptions(cmdArgs);
        cmdArgs.add(source.toString());

        // Offload the heavy lifting to our helper function
        return parser(cmdArgs, fileList);
    }

    /**
     * Performs some pre-defined operation and gathers the results for
     * the caller.
     *
     * @param cmdArgs the command line arguments to be used
     * @param fileList a list of files found or <code>null</code> if no
     *                 list is desired
     * @return <code>true</code> if successful, <code>false</code> on an
     *         error
     * @throws IllegalStateException if the <code>Rsync</code> object is
     *                               not yet setup for a transfer
     */
    private boolean parser(List<String> cmdArgs, List<FileListEntry> fileList) throws IllegalStateException {
        // Is this a blind execution?  (I.e. no feedback during the process?)
        if (this.countObservers() == 0) {
            logger.debug("No rsync observers registered, so no progress will be reported during the transfer");
        }

        // Now execute Expect4J
        Expect4j expect;
        if (logger.isDebugEnabled()) {
            String command = Joiner.on(' ').join(cmdArgs);
            logger.debug("Running rsync process: " + command);
        }

        RawOutputListener outputListener = new RawOutputListener();

        try {
            ProcessBuilder processBuilder =
                (new ProcessBuilder(cmdArgs))
                    .redirectErrorStream(true);

            String commandLine =
                cmdArgs
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));

            outputListener.bufferChanged(
                commandLine.toCharArray(),
                commandLine.length()
            );

            char[] newline = {'\n'};

            outputListener.bufferChanged(newline, newline.length);

            expect = new Expect4j(processBuilder.start());
        } catch (Exception e) {
            logger.warn("Caught exception while starting rsync: " + e);
            return false;
        }

        expect.registerBufferChangeLogger(outputListener);

        // Create a Closure pattern to handle errors
        Closure illegalStateHandler = new Closure() {
            public void run(ExpectState state) {
                RsyncIllegalStateError error = new RsyncIllegalStateError(state.getMatch());
                logger.warn("Detected state error in rsync transfer: " + error.getMessage());
                setChanged();
                notifyObservers(error);
                state.exp_continue_reset_timer();
                state.addVar("error", "true");
            }
        };

        Closure benignStateHandler = new Closure() {
            public void run(ExpectState state) {
                RsyncIllegalStateError error = new RsyncIllegalStateError(state.getMatch());
                logger.warn("Detected non-critical error in rsync transfer: " + error.getMessage());
                setChanged();
                notifyObservers(error);
                state.exp_continue_reset_timer();
            }
        };

        Closure socketHandler = new Closure() {
            public void run(ExpectState state) {
                RsyncSocketError error = new RsyncSocketError(state.getMatch());
                logger.warn("Detected socket error in rsync transfer: " + error.getMessage());
                setChanged();
                notifyObservers(error);
                state.exp_continue_reset_timer();
                state.addVar("error", "true");
            }
        };

        // Create a placeholder for our results
        final List<FileListEntry> results = fileList;

        // Loop on checking the rsync command output
        try {
            expect.expect(new Match[] {
                // Error Handling
                new RegExpMatch("rsync: failed to connect to \\S+ \\(\\S+\\): [^)]+\\)", socketHandler),
                new RegExpMatch("rsync error: error in socket IO \\(code \\d+\\) at \\S+ \\S+", socketHandler),
                new RegExpMatch("@ERROR: Unknown command '[^']+'", illegalStateHandler),
                new RegExpMatch("@ERROR: Unknown module \\S+", illegalStateHandler),
                new RegExpMatch("@ERROR: access denied to [^)]+\\)", illegalStateHandler),
                new RegExpMatch("@ERROR: auth failed on module \\S+", illegalStateHandler),
                new RegExpMatch("@ERROR: chdir failed", illegalStateHandler),
                new RegExpMatch("@ERROR: chroot failed", illegalStateHandler),
                new RegExpMatch("@ERROR: daemon security issue -- contact admin", illegalStateHandler),
                new RegExpMatch("@ERROR: failed to open lock file", illegalStateHandler),
                new RegExpMatch("@ERROR: fork failed", illegalStateHandler),
                new RegExpMatch("@ERROR: getpwuid failed", illegalStateHandler),
                new RegExpMatch("@ERROR: initgroups failed", illegalStateHandler),
                new RegExpMatch("@ERROR: invalid gid \\S+", illegalStateHandler),
                new RegExpMatch("@ERROR: invalid gid setting", illegalStateHandler),
                new RegExpMatch("@ERROR: invalid uid \\S+", illegalStateHandler),
                new RegExpMatch("@ERROR: max connections (\\d+) reached -- try again later", illegalStateHandler),
                new RegExpMatch("@ERROR: no path setting", illegalStateHandler),
                new RegExpMatch("@ERROR: setgid failed", illegalStateHandler),
                new RegExpMatch("@ERROR: setgroups failed", illegalStateHandler),
                new RegExpMatch("@ERROR: setuid failed", illegalStateHandler),
                new RegExpMatch("@ERROR: too many groups", illegalStateHandler),
                new RegExpMatch("@ERROR: your client is speaking an incompatible beta of protocol 30", illegalStateHandler),
                new RegExpMatch("ERROR: The remote path must start with a module name not a /", illegalStateHandler),
                new RegExpMatch("ERROR: module is read only", illegalStateHandler),
                new RegExpMatch("The source and destination cannot both be remote\\.", illegalStateHandler),
                new RegExpMatch("rsync error: error in rsync protocol data stream \\(code \\d+\\) at \\S+ \\S+", illegalStateHandler),
                new RegExpMatch("rsync error: error starting client-server protocol \\(code \\d+\\) at \\S+ \\S+", illegalStateHandler),
                new RegExpMatch("rsync error: received SIGINT, SIGTERM, or SIGHUP [^)]+\\) at [^)]+\\) [^]]+\\]", illegalStateHandler),
                new RegExpMatch("rsync error: received SIGUSR1 [^)]+\\) at [^)]+\\) [^]]+\\]", illegalStateHandler),
                new RegExpMatch("rsync error: some files/attrs were not transferred \\(see previous errors\\) \\(code \\d+\\) at \\S+ \\S+", illegalStateHandler),
                new RegExpMatch("rsync error: syntax or usage error \\(code \\d+\\) at \\S+ \\S+", illegalStateHandler),
                new RegExpMatch("rsync: change_dir \"[^\"]+\" \\(in \\S+\\) failed: [^)]+\\)", illegalStateHandler),
                new RegExpMatch("rsync: connection unexpectedly closed \\(\\d+ bytes received so far\\) [^]]+\\]", illegalStateHandler),
                new RegExpMatch("rsync: did not see server greeting", illegalStateHandler),
                new RegExpMatch("rsync: didn't get server startup line", illegalStateHandler),
                new RegExpMatch("rsync: failed to open \"[^\"]+\" \\(in \\S+\\), continuing: [^)]+\\)", benignStateHandler),
                new RegExpMatch("rsync: link_stat \\S+ failed: [^)]+\\)", illegalStateHandler),
                new RegExpMatch("rsync: opendir \"[^\"]+\" \\(in \\S+\\) failed: [^)]+\\)", illegalStateHandler),
                new RegExpMatch("rsync: read error: [^)]+\\)", illegalStateHandler),
                new RegExpMatch("rsync: send_files failed to open \"[^\"]+\" \\(in \\S+\\): [^)]+\\)", illegalStateHandler),
                new RegExpMatch("rsync: server is speaking an incompatible beta of protocol 30", illegalStateHandler),
                new RegExpMatch("rsync: server sent \"[^\"]+\" rather than greeting", illegalStateHandler),
                new RegExpMatch("rsync: writefd_unbuffered failed to write \\d+ bytes to socket [^:]+: [^)]+\\)", illegalStateHandler),
                // Now for the positive stuff
                new RegExpMatch("(sending|receiving) incremental file list", new Closure() {
                    public void run(ExpectState state) {
                        StartOfTransfer sot = new StartOfTransfer();
                        logger.debug("Detected start of file list exchange");
                        setChanged();
                        notifyObservers(sot);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("([-dlcbps][-r][-w][-xst][-r][-w][-xst][-r][-w][-xst]\\S?)\\s+(\\d+)\\s+(\\d+/\\d+/\\d+)\\s+(\\d+:\\d+:\\d+)\\s+([^\n]+)", new Closure() {
                    public void run(ExpectState state) {
                        String permissions = state.getMatch(1);
                        String size = state.getMatch(2);
                        String yyyymmdd = state.getMatch(3);
                        String hhmmss = state.getMatch(4);
                        String filename = state.getMatch(5);
                        FileListEntry fle = new FileListEntry(permissions, size, yyyymmdd, hhmmss, filename);
                        if (results != null)
                            results.add(fle);
                        logger.debug("Detected file listing: " + filename);
                        setChanged();
                        notifyObservers(fle);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("file: ([^\n]+)", new Closure() {
                    public void run(ExpectState state) {
                        String filename = state.getMatch(1);
                        StartOfFileTransfer sof = new StartOfFileTransfer(filename);
                        logger.debug("Detected start of next file: " + filename);
                        setChanged();
                        notifyObservers(sof);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("\\s+(\\d+(\\.\\d+)?\\S?)\\s+(\\d+(\\.\\d+)?)%\\s+(\\d+(\\.\\d+)?\\S?B/s)\\s+(\\d+:\\d+:\\d+)", new Closure() {
                    public void run(ExpectState state) {
                        String bytesTransferred = state.getMatch(1);
                        String percentComplete  = state.getMatch(3);
                        String transferRate     = state.getMatch(5);
                        String timeLeft         = state.getMatch(7);
                        FileTransferUpdate ftu = new FileTransferUpdate(
                            bytesTransferred,
                            percentComplete,
                            transferRate,
                            timeLeft
                        );
                        logger.debug("Detected file transfer update: " + bytesTransferred + " bytes (" + percentComplete + "%) at " + transferRate + ", " + timeLeft + " remaining");
                        setChanged();
                        notifyObservers(ftu);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("xfer#(\\d+),\\s+to-check=(\\d+)/(\\d+)", new Closure() {
                    public void run(ExpectState state) {
                        String xferNumber       = state.getMatch(1);
                        String xferRemaining    = state.getMatch(2);
                        String xferTotal        = state.getMatch(3);
                        EndOfFileTransfer eoft = new EndOfFileTransfer(xferNumber, xferRemaining, xferTotal);
                        logger.debug("Detected end of file transfer: finished file #" + xferNumber + ", " + xferRemaining + " remaining out of " + xferTotal);
                        setChanged();
                        notifyObservers(eoft);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("Number of files: (\\S+)\\s+" +
                                "Number of files transferred: (\\S+)\\s+" +
                                "Total file size: (\\S+) bytes\\s+" +
                                "Total transferred file size: (\\S+) bytes\\s+" +
                                "Literal data: (\\S+) bytes\\s+" +
                                "Matched data: (\\S+) bytes\\s+" +
                                "File list size: (\\S+)\\s+" +
                                "File list generation time: (\\S+) seconds\\s+" +
                                "File list transfer time: (\\S+) seconds\\s+" +
                                "Total bytes sent: (\\S+)\\s+" +
                                "Total bytes received: (\\S+)", new Closure() {
                    public void run(ExpectState state) {
                        String numFiles                 = state.getMatch(1);
                        String numFilesTransferred      = state.getMatch(2);
                        String totalFileSize            = state.getMatch(3);
                        String totalFileSizeTransferred = state.getMatch(4);
                        String literalData              = state.getMatch(5);
                        String matchedData              = state.getMatch(6);
                        String fileListSize             = state.getMatch(7);
                        String fileListGenerationTime   = state.getMatch(8);
                        String fileListTransferTime     = state.getMatch(9);
                        String totalBytesSent           = state.getMatch(10);
                        String totalBytesReceived       = state.getMatch(11);
                        TransferStats stats = new TransferStats(
                            numFiles,
                            numFilesTransferred,
                            totalFileSize,
                            totalFileSizeTransferred,
                            literalData,
                            matchedData,
                            fileListSize,
                            fileListGenerationTime,
                            fileListTransferTime,
                            totalBytesSent,
                            totalBytesReceived
                        );
                        logger.debug("Detected rsync transfer statistics");
                        setChanged();
                        notifyObservers(stats);
                        state.exp_continue_reset_timer();
                    }
                }),
                new RegExpMatch("sent (\\S+) bytes\\s+received (\\S+) bytes\\s+(\\S+) bytes/sec\\s+" +
                                "total size is (\\S+)\\s+speedup is (\\S+)", new Closure() {
                    public void run(ExpectState state) {
                        String bytesSent     = state.getMatch(1);
                        String bytesReceived = state.getMatch(2);
                        String transferRate  = state.getMatch(3);
                        String totalSize     = state.getMatch(4);
                        String speedup       = state.getMatch(5);
                        SentReceivedStats stats = new SentReceivedStats(
                            bytesSent,
                            bytesReceived,
                            transferRate,
                            totalSize,
                            speedup
                        );
                        logger.debug("Detected rsync summary statistics");
                        setChanged();
                        notifyObservers(stats);
                        state.exp_continue_reset_timer();
                    }
                }),
                // Normal exceptions
                new EofMatch(new Closure() {
                    public void run(ExpectState state) {
                        logger.debug("Detected EOF on rsync transfer stream");
                        setChanged();
                        notifyObservers(new RsyncEof(state.getBuffer()));
                        state.addVar("eof", "true");
                    }
                }),
                new TimeoutMatch(new Closure() {
                    public void run(ExpectState state) {
                        logger.debug("Detected timeout on rsync tranfser stream");
                        setChanged();
                        notifyObservers(new RsyncTimeout(state.getBuffer()));
                        state.addVar("timeout", "true");
                    }
                })
            });
        } catch (Exception e) {
            logger.warn("Caught exception", e);
        }

        // Clean up as best we can
        expect.close();

        // Return some result
        ExpectState lastState = expect.getLastState();
        if ((lastState.getVar("error") != null && Boolean.parseBoolean((String) lastState.getVar("error")) == true) ||
            (lastState.getVar("timeout") != null && Boolean.parseBoolean((String) lastState.getVar("timeout")) == true))
        {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Applies the {@link RsyncOption}s to the {@link java.util.List} of
     * command line arguments.
     *
     * @param cmdArgs the list of command arguments to which the
     *                decorators should be applied
     */
    private void applyOptions(List<String> cmdArgs) {
        for (RsyncOption option : safe(this.options))
            option.toRsyncArgument(cmdArgs);  // add the optional parameters next
    }

    /**
     * Clears the list of rsync options.
     */
    public void clearOptions() {
        options = null;
    }

    /**
     * Ensures that usage of a {@link List} occurs in a way that won't
     * generate a <code>NullPointerException</code>.  This is typically
     * used in Java for-each loops.
     *
     * <pre>for (Object o : safe(someList)) {
     *     ...
     * }</pre>
     *
     * @param list the <code>List</code> to sanitize
     * @return <code>list</code> if it is safe to use or an empty list if not
     */
    public static <T> List<T> safe(List<T> list) {
        return (list == null) ? new ArrayList<T>() : list;
    }
}

// vim: set ts=4 expandtab:
