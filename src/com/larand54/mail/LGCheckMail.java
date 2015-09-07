/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

/**
 *
 * @author Lars-G
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
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

public class LGCheckMail {

    private static int Period = 120; // Seconds between each check
    private static String mailFolder;
    private int noOfUnread;
    private MyMail mail;
    private Folder inbox;
    private Folder Egna;
    private Folder Carmak;

    public static void main(String[] args) {
        try {
            mailFolder = "Arbete/carmak";
            if (args.length > 0) {
                try {
                    Period = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    Period = 120;
                }
            }
            if (args.length > 1){
                mailFolder = args[1];
            }
            LGCheckMail ChkMail = new LGCheckMail();
            while (true) {
                ChkMail.Go(mailFolder);
                Thread.sleep(Period*1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LGCheckMail() {

    }

    private void Go(String aFolder) {
        /*  Set the mail properties  */
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            /*  Create the session and get the store for read the mail. */
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "larand54@gmail.com", "pZi6m82qA719tw5bY");

            /*  Mention the folder name which you want to read. */
            inbox = store.getFolder(aFolder);//("Arbete/carmak");//("Inbox");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            System.out.println("Folder: "+aFolder+" - No of Unread Messages : " + 
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
                printAllMessages(messages);
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
//            System.exit(2);
        }
    }

    public void printAllMessages(Message[] msgs) throws Exception {
        for (int i = 0; i < msgs.length; i++) {
            System.out.println("MESSAGE #" + (i + 1) + ":");
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
        frame.getContentPane().setBackground(Color.decode("#FF7070"));
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

class MyMail {
}
