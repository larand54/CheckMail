/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 *
 * @author larand
 */
public class CheckMailFolder extends TimerTask {
    private final String folderName;
    private final String backgroundColor;
    private final String user;
    private final String passw;
    private final String server;
    private final String name;
    private Folder inbox;
    
    public CheckMailFolder(String aName, String aFolderName, String aColor, String aUser, String aPassw, String aServer) {
        name = aName;
        folderName = aFolderName;
        backgroundColor = aColor;
        user = aUser;
        passw = aPassw;
        server = aServer;
    }
    
    @Override
    public void run() {
        Message[] messages;
        if  ((messages = checkForNewMail())!= null) {
            try {  
                printAllMessages(messages);
            } catch (Exception ex) {
                Logger.getLogger(CheckMailFolder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Message[] checkForNewMail() {
        Message messages[] = null;
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
            messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

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
        return messages;
        
    }
    
        public void printAllMessages(Message[] msgs) throws Exception {
        String from;
        Address[] a;
        for (int i = 0; i < msgs.length; i++) {
            a = msgs[i].getFrom();
            from = a == null ? null : ((InternetAddress) a[0]).getAddress();

            System.out.println(name + " -- MESSAGE #" + (i + 1) + ":" + " From: "+ from + " Subject: "+msgs[i].getSubject());
            printEnvelope(msgs[i],i,msgs.length);
        }
    }

    /*  Print the envelope(FromAddress,ReceivedDate,Subject)  */
    public void printEnvelope(Message message, int iMsgNo, int iNoOfMsg) throws Exception {
        Address[] a;
        String from = "Unknown";
        iMsgNo = iMsgNo + 1;
        String msgNo = String.valueOf(iMsgNo);
        String noOfMsg = String.valueOf(iNoOfMsg);
        // FROM
        a = message.getFrom();
        from = a == null ? null : ((InternetAddress) a[0]).getAddress();

        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
        MailMessage(from, subject, receivedDate.toString(), msgNo, noOfMsg); //    System.out.println("Content : " + content);
                //    getContent(message);
    }

    public void MailMessage(String aFrom, String aSubject, String aDate, String aMsgNo, String aNoOfMsg) {
        String message = "Subject: " + aSubject;
        String header = "Msg no: " + aMsgNo + "(" + aNoOfMsg + ")" + " Mail From: " + aFrom + " at " + aDate;
        final JFrame frame = new JFrame();
        frame.setSize(500, 125);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.decode(backgroundColor));
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel headingLabel = new JLabel(header);
        Icon headingIcon = null;
        headingLabel.setIcon(headingIcon); // --- use image icon you want to be as heading image.
        headingLabel.setOpaque(false);
        frame.add(headingLabel, constraints);
        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
        JButton cloesButton = new JButton(new AbstractAction("X") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.dispose();
            }
        });
        cloesButton.setMargin(new Insets(1, 4, 1, 4));
        cloesButton.setFocusable(false);
        frame.add(cloesButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel messageLabel = new JLabel("<HtMl>" + message
        );
        frame.add(messageLabel, constraints);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
        frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
        frame.setVisible(true);
    }

    
}
