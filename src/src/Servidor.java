package src;

import org.json.simple.JSONArray;

import javax.swing.*;
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
    private String config;

    ReadJSON readJSON = new ReadJSON();
    public Servidor() {
        puerto = 5000;
        conectarActivo = true;

        try {
            readJSON.readFile("src/datos/data13-41.json");
            config = readJSON.getPrimerMensaje();
            enviarRutinaConexion('0');
            //readJSON.getRutinaPlan();
            //readJSON.getRutinaDesconexion();
        }catch(Exception e) {
            e.printStackTrace();

        }
    }

    public void enviarRutinaConexion(char seg) throws IOException {
        ArrayList<JSONArray> arr = readJSON.getRutinaConexion();
        JSONArray instrucciones1 = arr.get(0);
        JSONArray instrucciones2 = arr.get(1);

        Iterator iterator1 = instrucciones1.iterator();
        Iterator iterator2 = instrucciones2.iterator();

        String dato = "";

        String cod1 = buscarCodigo(iterator1, seg);
        String cod2 = buscarCodigo(iterator2, seg);

        if(cod1!=""){
            cod1 = cod1.replaceAll("^.","1");
            dato += cod1;
        }

        if(cod2!=""){
            cod2 = cod2.replaceAll("^.","2");
            dato += "-"+cod2;
        }

        System.out.println(dato);
    }

    public String buscarCodigo(Iterator iterator, char number){
        while(iterator.hasNext()){
            String codigo = iterator.next().toString();
            if(codigo.charAt(0)==number){
                return codigo;
            }
        }
        return "";
    }

    public void conectar() throws IOException {

        // Crear el servidor
        server = new ServerSocket(puerto);

        while(conectarActivo){


            // Esperar a que alguien se conecte
            cliente = server.accept();
            // Alguien se conectó
            //datosEntrada = new DataInputStream(cliente.getInputStream());
            // reciba valor del cliente para desconectar
            datosSalida = new DataOutputStream(cliente.getOutputStream());
            datosSalida.writeUTF(config);
            enviarRutinaConexion('0');

            datosSalida.close();
            server.close();
            System.out.println("Conexion terminada");
        }

    }

}
