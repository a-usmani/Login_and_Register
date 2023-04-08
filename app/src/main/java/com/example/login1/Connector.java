package com.example.login1;

public class Connector {
    private static String ip = "";
    private static String un = "";
    private static String pass = "";
    private static String db = "";
    private static String port = "";


    protected String getIp(){
        return ip;
    }
    protected String getUn(){
        return un;
    }
    protected String getPass(){
        return pass;
    }
    protected String getDb(){
        return db;
    }
    protected String getPort(){
        return port;
    }
}
