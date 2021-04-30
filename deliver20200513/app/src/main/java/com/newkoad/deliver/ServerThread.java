package com.newkoad.deliver;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;


public class ServerThread  extends  Thread {

    Handler handler;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 9990;


    int count = 0;
    public ServerThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {

        try {

            serverSocket = new ServerSocket(socketServerPORT);


            while (true) {



                Socket socket = serverSocket.accept();

                count++;
                message = "#" + count + " from " + socket.getInetAddress() + ":"  + socket.getPort() + "  ";




                String line = "";
                String msg ="";
                int cnt = 0;
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream() , "UTF-8"));

                    while ((line = br.readLine()) != null) {


                        msg += line;
                    }


                    JSONObject json = new JSONObject(msg);
                    String adPlay = json.getString("order");

                    if(adPlay.equals("refresh")){
                        Message hmsg = Message.obtain(handler, 1);
                        handler.sendMessage(hmsg);

                    }else if(adPlay.equals("stop")){
                        Message hmsg = Message.obtain(handler, 2);
                        handler.sendMessage(hmsg);

                    }else if(adPlay.equals("set_open")){
                        Message hmsg = Message.obtain(handler, 3);
                        handler.sendMessage(hmsg);

                    }else if(adPlay.equals("set_close")){
                        Message hmsg = Message.obtain(handler, 4);
                        handler.sendMessage(hmsg);

                    }


                }catch(IOException e) {
                    System.out.println(e);
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void JsonProcess( String msg){





    }


    private class ServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        ServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Hello from Server, you are #" + cnt;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();
                hostThreadSocket.close();

                message += "replayed: " + msgReply + "\n";




            } catch (IOException e) {

                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }



        }

    }





}
