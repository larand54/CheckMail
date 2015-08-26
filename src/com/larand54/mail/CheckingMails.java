/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/*
  Message[] msgs = folder.getMessages();

  FetchProfile fp = new FetchProfile();
  fp.add(FetchProfile.Item.ENVELOPE);
  fp.add("X-mailer");
  folder.fetch(msgs, fp);
*/


public class CheckingMails {

    /**
     * @param args the command line arguments
     */
    public static LinkedHashMap flaggedMessages; 
    public static String getMD5(String input) throws NoSuchAlgorithmException {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (int i = 0; i < input.length() - 1; i++) {
                char c = input.charAt(i);
                md.update((byte) c);
            }
            byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e.getMessage());
            System.out.println("File: " + input);
            return "Error-1";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File: " + input);
            return "Error-2";
        } finally {
        }
    }

    private static String msgID(Message msg) throws MessagingException {
        String mID = "";
        Enumeration headers = msg.getAllHeaders();
        while (headers.hasMoreElements()) {
            Header h = (Header) headers.nextElement();
            mID = h.getName();
            if (mID.contains("Message-ID")) {
                mID = mID + ":  " + h.getValue();
                return mID;
            } else {
                mID = "";
            }
        };

        if (mID == "") {
            mID = "md5:";
            Enumeration hdrs = msg.getAllHeaders();
            while (hdrs.hasMoreElements()) {
                Header hd = (Header) hdrs.nextElement();
                switch (hd.getName()){
                    case "Delivered-To":
                    case "Received":
                    case "From":
                    case "To":
                    case "Subject":
                    case "Thread-Topic":
                    case "Thread-Index":
                    case "Date":
                    case "Mime-Version":
                    case "In-Reply-To":
                    case "Referencies":
                    case "ReturnPath":
                        mID = mID + hd.getValue();
                    
                }
            }
            try {
                mID = "MD5"+getMD5(mID);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CheckingMails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return mID;
    }

    public static void check(String host, String storeType, String user, String password) {
        try {
            // Create properies field
            Properties properties = new Properties();
            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls", "true");

            Session emailSession = Session.getDefaultInstance(properties);

            // create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");
            store.connect(host, user, password);

            // create the folder and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length: " + messages.length);
            
            flaggedMessages = new LinkedHashMap(); 
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                flaggedMessages.put(msgID(message), message);
                System.out.println("-----------------------------------");
                System.out.println("Email number " + (i + 1));
                System.out.println("Message-ID   " + msgID(message));
                System.out.println("Subject      " + message.getSubject());
                System.out.println("From         " + message.getFrom()[0]);
                System.out.println("Text         " + message.getContent().toString());
                Date recDate = message.getReceivedDate();
                if (recDate != null) {
                    System.out.println("ReceivedDate " + message.getReceivedDate().toString());
                }
           
                try {           
                    System.out.println("SentDate     " + message.getSentDate());
                } catch (Exception e) {
                
                }
            }

            // close the store and folder objects
            emailFolder.close(false);
            store.close();
            System.out.println("Antal poster: "+flaggedMessages.size());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        String host = "pop.gmail.com";
        String mailStoreType = "pop3";
        String username = "larand54@gmail.com";
        String password = "pZi6m82qA719tw5bY";

        check(host, mailStoreType, username, password);
    }

}
