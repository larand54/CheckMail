/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.larand54.mail;

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
        
    }
    
}
