package src;

import org.json.simple.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor{
    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    private DataInputStream datosEntrada;
    private DataOutputStream datosSalida;
    private boolean conectarActivo;

    ReadJSON readJSON = new ReadJSON();
    public Servidor() {
        puerto = 5000;
        conectarActivo = true;
        try {
            readJSON.readFile("src/datos/data13-41.json");
            readJSON.getPrimerMensaje();

            //readJSON.getRutinaPlan();
            //readJSON.getRutinaDesconexion();
        }catch(Exception e) {
            e.printStackTrace();

        }
    }

    public void enviarRutinaConexion() throws IOException {
        ArrayList<JSONArray> arr = readJSON.getRutinaConexion();
        Iterator iterator = arr.iterator();
        while(iterator.hasNext()){
            JSONArray aux = (JSONArray) iterator.next();
            Iterator auxIterator = aux.iterator();
            while (auxIterator.hasNext()){
                String codigo = auxIterator.next().toString();
                System.out.println(codigo);
                datosSalida.writeUTF(codigo);
            }
        }
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
            enviarRutinaConexion();
            datosSalida.writeInt(14);
            datosSalida.close();
            server.close();
            System.out.println("Conexion terminada");
        }

    }
}
