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
public class XThread extends Thread {
    public static boolean delay(long millis) {
        if (interrupted())
            return false;
        try {
            sleep(millis);
        }
        catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
