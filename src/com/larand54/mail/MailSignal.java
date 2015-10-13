/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

import java.util.Timer;

/**
 *
 * @author larand
 */
public class MailSignal {

    public static CheckMailFolder m1, m2, m3, m4;
    public static Timer t = new Timer();

    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        m1 = new CheckMailFolder("carmak", "Arbete/carmak", "#FF7070", "larand54@gmail.com", "pZi6m82qA719tw5bY", "imap.gmail.com");
        m2 = new CheckMailFolder("Personligt", "Personligt", "#70FF70", "larand54@gmail.com", "pZi6m82qA719tw5bY", "imap.gmail.com");
        m3 = new CheckMailFolder("COCONUT", "Coconut", "#7070FF", "larand54@yahoo.se", "Millie0623", "imap.mail.yahoo.com");
        m4 = new CheckMailFolder("MARKETS", "Markets.com", "#FFFF50", "larand54@yahoo.se", "Millie0623", "imap.mail.yahoo.com");

        t = new Timer();
            t.schedule(m1, 0, 120000);
            t.schedule(m2, 9000, 120000);
            t.schedule(m3, 20000, 120000);
            t.schedule(m4, 39000, 120000);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() { /*
                     my shutdown code here */

                    m1.cancel();
                    m2.cancel();
                    m3.cancel();
                    m4.cancel();
                    t.cancel();
                }
            });
    }

}
