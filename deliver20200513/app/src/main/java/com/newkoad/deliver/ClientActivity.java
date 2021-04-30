package com.newkoad.deliver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.newkoad.deliver.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {


    TextView response;
    EditText ipText, portText, nameText;
    Button buttonConnect, buttonClear, buttonSend;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        ipText = (EditText) findViewById(R.id.ipText);
        portText = (EditText) findViewById(R.id.portText);
        nameText = (EditText) findViewById(R.id.nameText);


        ipText.setText("");
        portText.setText("");


        response = (TextView) findViewById(R.id.responseTextView);

        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Client client = new Client(ipText.getText().toString(), portText.getText().toString());
                client.start();
            }
        });


        buttonSend = (Button) findViewById(R.id.sendButton);
        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SendThread sendThread = new SendThread(socket);
                sendThread.start();


            }
        });

        buttonClear = (Button) findViewById(R.id.clearButton);
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });


    }


    class Client extends Thread {

        boolean threadAlive;
        String ip;
        String port;
        String mac;

        ReceiveThread receiveThread;
        SendThread sendThread;

        DataOutputStream dataOutputStream;

        Client(String ip, String port) {

            threadAlive = true;
            this.ip = ip;
            this.port = port;


        }

        @Override
        public void run() {
            try { socket = new Socket(ip, Integer.parseInt(port));



                receiveThread = new ReceiveThread(socket);




                receiveThread.start();




            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    class ReceiveThread extends Thread {

        Socket socket = null;
        DataInputStream dataInputStream;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {

            }
        }


        public void run() {
            try {
                while (dataInputStream != null) {
                    String msg = dataInputStream.readUTF();
                    if (msg != null) {

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {

            Socket socket;
            String sendmsg = "  ";
            DataOutputStream dataOutputStream;

            PrintWriter printWriter;

            SendThread(Socket socket){

                this.socket = socket;
                try{
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                }catch (Exception e){

                }
            }

            public void run(){

                try{

                    String mac = null;
                    if(dataOutputStream != null){
                        if(sendmsg != null){
                            dataOutputStream.writeUTF( "jupiter" + sendmsg + "\n");

                        }
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }


    }
}
