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
 * com.github.cverges.rsync.Rsync} to perform the
 * transfer across an SSH tunnel.  It is equivalent to the <code>-e
 * &quot;ssh -l username -i keyfile -p port&quot;</code> argument for
 * <code>rsync</code>.
 * <p>
 * Note that this implementation does not currently allow for password
 * authentication.  All user authentication must be done via RSA keys.
 *
 * @author Chris Verges
 * @since 1.0
 */
public class SshTunnel implements RsyncOption {
    /**
     * The username to use when logging into the SSH session.
     */
    private String username;

    /**
     * The path to the SSH key used for authentication.
     */
    private String privateKey;

    /**
     * The TCP port of the running SSH server.  The address of the
     * server is assumed to be that specified in the {@link com.github.cverges.rsync.Rsync} URI.
     */
    private int port;

    /**
     * Creates a <code>SshTunnel</code> instance with the given
     * username and private key.  The TCP port of the SSH server is
     * assumed to be 22.
     *
     * @param username the username to use when logging into the SSH
     *                 session
     * @param privateKey the path to the SSH private key used for
     *                   authentication
     */
    public SshTunnel(String username, String privateKey) {
        this(username, privateKey, 22);
    }

    /**
     * Creates a <code>SshTunnel</code> instance with the given
     * username, private key, and TCP port.
     *
     * @param username the username to use when logging into the SSH
     *                 session
     * @param privateKey the path to the SSH private key used for
     *                   authentication
     * @param port the TCP port of the SSH server
     */
    public SshTunnel(String username, String privateKey, int port) {
        this.username = username;
        this.privateKey = privateKey;
        this.port = port;
    }

    /**
     * Convert the option to an <code>rsync</code> command line argument.
     *
     * @param args the list of arguments to append
     */
    public void toRsyncArgument(List<String> args) {
        args.add("-e");
        args.add("\"ssh -l " + username + " -i " + privateKey + " -p " + port + "\"");
    }
}

// vim: set ts=4 expandtab:
