package src;

import org.json.simple.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;


public class Servidor implements Runnable {
    private Thread hiloRutinas;
    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    private DataInputStream datosEntrada;
    private DataOutputStream datosSalida;
    private boolean conectarActivo;
    private ArrayList<ArrayList <int []>> allCardsLeds = new ArrayList<>();
    private String infoRutina;
    private String infoCod1;
    private String infoCod2;
    ReadJSON readJSON = new ReadJSON();
    public Servidor() {
        puerto = 5000;
        conectarActivo = true;
        hiloRutinas = new Thread(this);
    }

    public String leerConfiguracion() {
        // Leer configuracion
        String config = "";
        try {
            readJSON.readFile("src/datos/data13-41.json");
            config = readJSON.getPrimerMensaje();
            System.out.println("Tiempo rutina: " + readJSON.getTiempoRutina());
            //readJSON.getRutinaPlan();
            //readJSON.getRutinaDesconexion();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    public void enviarRutinaConexion(String seg) {
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

        if (!dato.equals(""))
            infoRutina = dato;

        // Enviar y recibir
        String cantLedsFuncionando;
        try {
            datosSalida.writeUTF(infoRutina);
        } catch (IOException e) {
            System.out.println("Error en el envio de la rutina de conexion");
            throw new RuntimeException(e);
        }

        try {
            cantLedsFuncionando = datosEntrada.readUTF();
            System.out.println("Servidor recibe: " + cantLedsFuncionando);
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }

    public void enviarRutinaNormal(String seg) {
        ArrayList<JSONArray> arr = readJSON.getRutinaPlan();
        JSONArray instrucciones1 = arr.get(0);
        JSONArray instrucciones2 = arr.get(1);

        Iterator iterator1 = instrucciones1.iterator();
        Iterator iterator2 = instrucciones2.iterator();

        String dato = "";

        String cod1 = buscarCodigo(iterator1, seg);
        System.out.println("Cod1 " + cod1);

        String cod2 = buscarCodigo(iterator2, seg);
        System.out.println("Cod2 " + cod2);

        if(cod1!=""){
            cod1 = cod1.replaceAll("^.","1");
            dato += cod1;
        }

        if(cod2!=""){
            cod2 = cod2.replaceAll("^.","2");
            dato += "-" + cod2;
        }

        if (!dato.equals(""))
            infoRutina = dato;

        // Enviar y recibir
        String cantLedsFuncionando;
        try {
            System.out.println("Servidor envia: " + infoRutina);
            datosSalida.writeUTF(infoRutina);
        } catch (IOException e) {
            System.out.println("Error en el envio de la rutina de conexion");
            throw new RuntimeException(e);
        }

        try {
            cantLedsFuncionando = datosEntrada.readUTF();
            System.out.println("Servidor recibe: " + cantLedsFuncionando);
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }


    public void enviarRutinaDesconexion(String seg) {
        ArrayList<JSONArray> arr = readJSON.getRutinaDesconexion();
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

        System.out.println("Desconexion : "+dato);
    }

    public String buscarCodigo(Iterator iterator, String number){
        int i = 0;
        while(iterator.hasNext()){
            String codigo = iterator.next().toString();
            String codigoArr[] = codigo.split(":");
            if(number.equals(codigoArr[0])){
                return codigo;
            }
        }
        return "";
    }

    public void conectar() {
        try {
            // Crear el servidor
            server = new ServerSocket(puerto);
            // Esperar a que alguien se conecte
            cliente = server.accept();
            // Alguien se conectó - se capturan flujos
            datosEntrada = new DataInputStream(cliente.getInputStream());
            datosSalida = new DataOutputStream(cliente.getOutputStream());

            transmitirConfiguracionInicial();
            hiloRutinas.start();
            /*Timer timer = new Timer();
            TimerTask taskConexion = new TaskRutinaConexion(14);
            *//*Timer timer2 = new Timer();
            TimerTask taskDesconexion = new TaskRutinaDesconexion(14);*//*

            timer.schedule(taskConexion, 200, 1000);
            //System.out.println("inicia desconexion");
            //timer2.schedule(taskDesconexion, 200, 1000);*/


        } catch (IOException e) {
            System.out.println("Error en la conexion");
            throw new RuntimeException(e);
        }
    }

    public void transmitirConfiguracionInicial() {

        String cantLeds;
        try {
            datosSalida.writeUTF(leerConfiguracion());
            cantLeds = datosEntrada.readUTF();
        } catch (IOException e) {
            System.out.println("Error en la transmision de la configuracion inicial");
            throw new RuntimeException(e);
        }

        String[] leds = cantLeds.split("-");
        int[] ledsInt = null;
        ArrayList<int []> cardLeds = new ArrayList<>();
        for (int i = 0, j=0; i < leds.length; i++, j++) {
            if (i % 3 == 0) {
                if (i != 0) {
                    cardLeds.add(ledsInt);
                    if(cardLeds.size() == 2) {
                        allCardsLeds.add(cardLeds);
                        cardLeds = new ArrayList<>();
                    }
                }
                ledsInt = new int[3];
                j = 0;
            }
            ledsInt[j] = Integer.parseInt(leds[i]);
        }
    }

    public void desconectar() {
        try {
            datosSalida.close();
            server.close();
            System.out.println("Conexion terminada");
        } catch (IOException e) {
            System.out.println("Error en la desconexion");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // Rutina de conexion
        Long tiempoRutina = readJSON.getTiempoRutinaConexion();
        /*for (int i = 0; i < tiempoRutina; i++) {
            enviarRutinaConexion(Integer.toString(i));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/

        // Rutina normal
        tiempoRutina = readJSON.getTiempoRutina();
        for (int i = 0; i < tiempoRutina; i++) {
            enviarRutinaNormal(Integer.toString(i));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private class TaskRutinaConexion extends TimerTask {
        public static int i=0;
        int tiempoMaximo;
        TaskRutinaConexion(int tiempoMaximo)
        {
            this.tiempoMaximo = tiempoMaximo;
        }

        public void run(){
            if(i<tiempoMaximo)
                enviarRutinaConexion(Integer.toString(i));
            else
                cancel();
            i++;
        }
    }

    private class TaskRutinaDesconexion extends TimerTask{
        public static int i=0;
        int tiempoMaximo;
        TaskRutinaDesconexion(int tiempoMaximo)
        {
            this.tiempoMaximo = tiempoMaximo;
        }

        public void run(){
            if(i<tiempoMaximo)
                enviarRutinaDesconexion(Integer.toString(i));
            else
                cancel();
            i++;
        }
    }


}
