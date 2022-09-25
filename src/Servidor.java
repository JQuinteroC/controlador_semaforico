package src;

import javax.swing.*;
import java.awt.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends JFrame implements Runnable{

    private  JTextArea areaTexto;
    public Servidor(){
        setBounds(1200, 300, 400, 350);
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        areaTexto = new JTextArea();

        panel.add(areaTexto, BorderLayout.CENTER);

        add(areaTexto);

        setVisible(true);

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        //System.out.println("estoy escuchando");
        try {
            ServerSocket servidor = new ServerSocket(9999);

            while(true){
                Socket suck = servidor.accept();

                DataInputStream flujo_entrada = new DataInputStream(suck.getInputStream());

                String msg_entrada = flujo_entrada.readUTF();

                areaTexto.append("\n mensaje recibido: "+ msg_entrada);

                suck.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
