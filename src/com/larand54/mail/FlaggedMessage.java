/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;
import java.util.Date;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Header;

/**
 *
 * @author Lars-G
 */
public class FlaggedMessage {
    private String msgID;
    private Boolean Flagged;
    private Message msg;
    FlaggedMessage(Message amsg, String amsgID) {
        msgID = amsgID;
        msg = amsg;
        Flagged = false;
    }
    public void flagOff() {
        Flagged = true;
    }
    
    public Message message() {
        return msg;
    }
    
    public String MessID() {
        return msgID;
    }
    
    public Address[] From() throws MessagingException {
        return msg.getFrom();
    }
    
    public String Subject() throws MessagingException {
        return msg.getSubject();
    }
    
    public Date FromDate() throws MessagingException {
        return msg.getSentDate();
    }
    
}
