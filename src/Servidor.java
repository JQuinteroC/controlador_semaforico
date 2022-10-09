package src;

import javax.swing.*;
import java.awt.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor{
    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    private DataInputStream datosEntrada;
    private DataOutputStream datosSalida;
    private boolean conectarActivo;

    public Servidor() {
        puerto = 5000;
        conectarActivo = true;
    }

    public void conectar() throws IOException {

        // Crear el servidor
        server = new ServerSocket(puerto);

        while(conectarActivo){
            // Esperar a que alguien se conecte
            cliente = server.accept();
            // Alguien se conectó
            //datosEntrada = new DataInputStream(cliente.getInputStream());
            datosSalida = new DataOutputStream(cliente.getOutputStream());
            datosSalida.writeInt(14);
            datosSalida.close();
            server.close();
            System.out.println("Conexion terminada");
        }

    }
}
