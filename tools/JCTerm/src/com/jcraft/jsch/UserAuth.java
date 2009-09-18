/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2002,2003,2004,2005,2006 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.jcraft.jsch;

public abstract class UserAuth{
  protected static final int SSH_MSG_USERAUTH_REQUEST=               50;
  protected static final int SSH_MSG_USERAUTH_FAILURE=               51;
  protected static final int SSH_MSG_USERAUTH_SUCCESS=               52;
  protected static final int SSH_MSG_USERAUTH_BANNER=                53;
  protected static final int SSH_MSG_USERAUTH_INFO_REQUEST=          60;
  protected static final int SSH_MSG_USERAUTH_INFO_RESPONSE=         61;
  protected static final int SSH_MSG_USERAUTH_PK_OK=                 60;

  protected UserInfo userinfo;
  public boolean start(Session session, UserInfo userinfo) throws Exception{
    this.userinfo=userinfo;
    Packet packet=session.packet;
    Buffer buf=session.buf;
    // send
    // byte      SSH_MSG_SERVICE_REQUEST(5)
    // string    service name "ssh-userauth"
    packet.reset();
    buf.putByte((byte)Session.SSH_MSG_SERVICE_REQUEST);
    buf.putString("ssh-userauth".getBytes());
    session.write(packet);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO, 
                           "SSH_MSG_SERVICE_REQUEST sent");
    }

    // receive
    // byte      SSH_MSG_SERVICE_ACCEPT(6)
    // string    service name
    buf=session.read(buf);
    //System.err.println("read: 6 ? "+buf.buffer[5]);
    boolean result=(buf.buffer[5]==6);

    if(JSch.getLogger().isEnabled(Logger.INFO)){
      JSch.getLogger().log(Logger.INFO, 
                           "SSH_MSG_SERVICE_ACCEPT received");
    }

    return result;
  }
}
