/**
 * Copyright 2009, Frederic Bregier, and individual contributors by the @author
 * tags. See the COPYRIGHT.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3.0 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package goldengate.ftp.core.command.service;

import goldengate.common.command.ReplyCode;
import goldengate.common.command.exception.CommandAbstractException;
import goldengate.common.command.exception.Reply501Exception;
import goldengate.common.command.exception.Reply553Exception;
import goldengate.ftp.core.command.AbstractCommand;
import goldengate.ftp.core.exception.FtpNoFileException;
import goldengate.ftp.core.exception.FtpNoTransferException;
import goldengate.ftp.core.session.FtpFile;

/**
 * RNTO command
 *
 * @author Frederic Bregier
 *
 */
public class RNTO extends AbstractCommand {

    /*
     * (non-Javadoc)
     *
     * @see goldengate.ftp.core.command.AbstractCommand#exec()
     */
    public void exec() throws CommandAbstractException {
        if (!hasArg()) {
            invalidCurrentCommand();
            throw new Reply501Exception("Need a pathname as argument");
        }
        String filename = getArg();
        FtpFile file = null;
        try {
            file = getSession().getDataConn().getFtpTransferControl()
                    .getExecutingFtpTransfer().getFtpFile();
        } catch (FtpNoFileException e) {
        } catch (FtpNoTransferException e) {
        }
        if (file != null) {
            String previousName = file.getFile();
            if (file.renameTo(filename)) {
                getSession().setReplyCode(
                        ReplyCode.REPLY_250_REQUESTED_FILE_ACTION_OKAY,
                        "\"" + filename + "\" as new file name for \"" +
                                previousName + "\"");
                return;
            }
        }
        // FtpFile name not allowed or not found
        throw new Reply553Exception("Filename not allowed");
    }

}
