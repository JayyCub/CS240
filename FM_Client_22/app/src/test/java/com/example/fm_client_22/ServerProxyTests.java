package com.example.fm_client_22;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ReqRes.LoginRequest;
import ReqRes.RegisterRequest;

public class ServerProxyTests {
    DataCache dataCache = DataCache.getInstance();
    private final RegisterRequest regReq = new RegisterRequest("jacob", "pass",
            "email", "Jacob", "Thomsen", 'm');
    private final LoginRequest logReq = new LoginRequest("jacob", "pass");

    @Test
    public void registration(){
        System.out.println("REGISTRATION TEST\nStarting registration test...");
        ServerProxy serverProxy = new ServerProxy("localhost", "8080");
        System.out.println("Clearing Database...");
        assertTrue(serverProxy.clearDatabase());
        System.out.println("Attempting to register user...");
        assertTrue(serverProxy.register(regReq));
        System.out.println("Success!\n\n");
    }

    @Test
    public void login(){
        System.out.println("LOGIN TEST\nStarting login test...");
        ServerProxy serverProxy = new ServerProxy("localhost", "8080");
        System.out.println("Clearing Database...");
        assertTrue(serverProxy.clearDatabase());
        System.out.println("Registering user...");
        assertTrue(serverProxy.register(regReq));
        System.out.println("Registered user, logging in now...");
        assertTrue(serverProxy.login(logReq));
        System.out.println("Success!\n\n");
    }

    @Test
    public void people(){
        System.out.println("PEOPLE TEST\nStarting test on gathering associated people...");
        ServerProxy proxy = new ServerProxy("localhost", "8080");
        System.out.println("Clearing Database...");
        assertTrue(proxy.clearDatabase());
        assertTrue(dataCache.people.isEmpty());
        System.out.println("Attempting to register user...");
        assertTrue(proxy.register(regReq));
        System.out.println("Getting person's people...");
        proxy.getPersonName();
        proxy.getAllPersonData();
        System.out.println("Checking if data was stored in DataCache...");
        assertFalse(dataCache.people.isEmpty());
        System.out.println("Success!\n\n");
    }

    @Test
    public void events(){
        System.out.println("EVENTS TEST\nStarting test on gathering associated events...");
        ServerProxy proxy = new ServerProxy("localhost", "8080");
        System.out.println("Clearing Database...");
        assertTrue(proxy.clearDatabase());
        assertTrue(dataCache.people.isEmpty());
        System.out.println("Attempting to register user...");
        assertTrue(proxy.register(regReq));
        System.out.println("Getting person's events...");
        proxy.getPersonName();
        proxy.getAllPersonData();
        System.out.println("Checking if events were stored in DataCache...");
        assertFalse(dataCache.events.isEmpty());
        System.out.println("Success!\n\n");
    }
}
