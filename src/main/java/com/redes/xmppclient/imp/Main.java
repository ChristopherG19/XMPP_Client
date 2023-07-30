package com.redes.xmppclient.imp;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws XmppStringprepException {

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("garcia20541redes","Christopher1910")
                .setXmppDomain("alumchat.xyz")
                .setHost("alumchat.xyz")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
            connection.disconnect();
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

