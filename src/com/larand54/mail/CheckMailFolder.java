/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 *
 * @author larand
 */
public class CheckMailFolder implements Runnable {
    public Thread activity = new Thread(this);
    private final String folderName;
    private final long interval;
    private final String backgroundColor;
    private final String user;
    private final String passw;
    private final String server;
    private final String name;
    private Folder inbox;
    
    public CheckMailFolder(String aName, String aFolderName, String aColor, int aInterval, String aUser, String aPassw, String aServer) {
        name = aName;
        folderName = aFolderName;
        backgroundColor = aColor;
        interval = aInterval * 1000; // millisec to sec.
        user = aUser;
        passw = aPassw;
        server = aServer;
    }
    
    @Override
    public void run() {
        while (XThread.delay(interval)) {
           checkForNewMail();  
        }
    }
    
    private void checkForNewMail() {
        /*  Set the mail properties  */
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            /*  Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(server, user, passw);

            /*  Mention the folder name which you want to read. */
            inbox = store.getFolder(folderName);//("Arbete/carmak");//("Inbox");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            System.out.println("Folder: "+folderName+" - No of Unread Messages : " + 
                    "("+inbox.getUnreadMessageCount()+").  Kl: " + 
                    sdf.format(new Date(System.currentTimeMillis())));

            /*Open the inbox using store.*/
            inbox.open(Folder.READ_ONLY);

            /*  Get the messages which is unread in the Inbox*/
            Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            /* Use a suitable FetchProfile    */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            //        fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);

            try {
//                printAllMessages(messages);
                inbox.close(true);
                store.close();
            } catch (Exception ex) {
                System.out.println("Exception arise at the time of read mail");
                ex.printStackTrace();
            }
        } catch (NoSuchProviderException e) {
            System.out.println("NoSuchProviderException");
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            System.out.println("MessagingException");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
    }
    
}
