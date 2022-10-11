package src;

import org.json.simple.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor implements Runnable {

    private Thread hiloRutinas;
    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    private DataInputStream datosEntrada;
    private DataOutputStream datosSalida;
    private boolean conectarActivo;
    private ArrayList<ArrayList<int[]>> allCardsLeds = new ArrayList<>();
    private String infoRutina;
    private ArrayList<String> infoCod = new ArrayList<>();
    private boolean dano;
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
            readJSON.readFile("src/datos/data80-43.json");
            config = readJSON.getPrimerMensaje();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private boolean validarFuncionamientoDeLeds(String cantLedsFuncionando) {
        String[] ledsXTarjetas = cantLedsFuncionando.split("/");
        for (int i = 0; i < ledsXTarjetas.length; i++) {
            ledsXTarjetas[i] = ledsXTarjetas[i].replaceAll("-", "");
            ledsXTarjetas[i] = ledsXTarjetas[i].replaceAll(":", "");
            if (!limpiarNroLedsFuncionandoXTarjeta(infoCod.get(i)).equals(ledsXTarjetas[i])) {
                System.out.println(limpiarNroLedsFuncionandoXTarjeta(infoCod.get(i)) + " " + ledsXTarjetas[i]);
                return false;
            }
        }
        return true;
    }

    private String limpiarNroLedsFuncionandoXTarjeta(String cantLedsXTarjeta) {
        String nroLedsFiltrado = cantLedsXTarjeta.substring(3);
        StringBuffer sB = new StringBuffer(nroLedsFiltrado).deleteCharAt(3);
        return sB.toString();
    }

    public void enviarRutinaConexion(String seg) {
        ArrayList<JSONArray> arr = readJSON.getRutinaConexion();
        String dato = "";

        for (int i = 0; i < arr.size(); i++) {
            Iterator iterator = arr.get(i).iterator();
            String cod = buscarCodigo(iterator, seg);
            if (!"".equals(cod)) {
                cod = cod.replaceAll("^(\\d*)", Integer.toString(i + 1));
                if (infoCod.size() >= i+1) {
                    infoCod.set(i, cod);
                } else {
                    infoCod.add(cod);
                }
            }

            if (i == 0) {
                dato += infoCod.get(i);
            } else {
                dato += "-" + infoCod.get(i);
            }
        }

        if (!dato.equals("")) {
            infoRutina = dato;
        }

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
            if (!validarFuncionamientoDeLeds(cantLedsFuncionando)) {
                System.out.println("Led danado");
            }
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }

    public void enviarRutinaNormal(String seg) {
        ArrayList<JSONArray> arr = readJSON.getRutinaPlan();
        String dato = "";

        for (int i = 0; i < arr.size(); i++) {
            Iterator iterator = arr.get(i).iterator();
            String cod = buscarCodigo(iterator, seg);
            if (!"".equals(cod)) {
                cod = cod.replaceAll("^(\\d*)", Integer.toString(i + 1));
                                if (infoCod.size() >= i+1) {
                    infoCod.set(i, cod);
                } else {
                    infoCod.add(cod);
                }
            }

            if (i == 0) {
                dato += infoCod.get(i);
            } else {
                dato += "-" + infoCod.get(i);
            }
        }

        if (!dato.equals("")) {
            infoRutina = dato;
        }

        // Enviar y recibir
        String cantLedsFuncionando;
        try {
            datosSalida.writeUTF(infoRutina);
        } catch (IOException e) {
            System.out.println("Error en el envio de la rutina normal");
            throw new RuntimeException(e);
        }

        try {
            cantLedsFuncionando = datosEntrada.readUTF();
            if (!validarFuncionamientoDeLeds(cantLedsFuncionando)) {
                System.out.println("Led danado");
            }
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }

    public void enviarRutinaDesconexion(String seg) {
        ArrayList<JSONArray> arr = readJSON.getRutinaDesconexion();
        String dato = "";

        for (int i = 0; i < arr.size(); i++) {
            Iterator iterator = arr.get(i).iterator();
            String cod = buscarCodigo(iterator, seg);
            if (!"".equals(cod)) {
                cod = cod.replaceAll("^(\\d*)", Integer.toString(i + 1));
                                if (infoCod.size() >= i+1) {
                    infoCod.set(i, cod);
                } else {
                    infoCod.add(cod);
                }
            }

            if (i == 0) {
                dato += infoCod.get(i);
            } else {
                dato += "-" + infoCod.get(i);
            }
        }

        if (!dato.equals("")) {
            infoRutina = dato;
        }

        // Enviar y recibir
        String cantLedsFuncionando;
        try {
            datosSalida.writeUTF(infoRutina);
        } catch (IOException e) {
            System.out.println("Error en el envio de la rutina de desconexion");
            throw new RuntimeException(e);
        }

        try {
            cantLedsFuncionando = datosEntrada.readUTF();
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }

    private void enviarRutinaDano() {
        String cantLedsFuncionando;
        try {
            datosSalida.writeUTF("1:10101010-2:10101010");
        } catch (IOException e) {
            System.out.println("Error en el envio de la rutina de desconexion");
            throw new RuntimeException(e);
        }

        try {
            cantLedsFuncionando = datosEntrada.readUTF();
        } catch (IOException e) {
            System.out.println("Error en el recaudo de los leds funcionando");
            throw new RuntimeException(e);
        }
    }

    public String buscarCodigo(Iterator iterator, String number) {
        int i = 0;
        while (iterator.hasNext()) {
            String codigo = iterator.next().toString();
            String codigoArr[] = codigo.split(":");
            if (number.equals(codigoArr[0])) {
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
        ArrayList<int[]> cardLeds = new ArrayList<>();
        for (int i = 0, j = 0; i < leds.length; i++, j++) {
            if (i % 3 == 0) {
                if (i != 0) {
                    cardLeds.add(ledsInt);
                    if (cardLeds.size() == 2) {
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

    private void delay(int mls) {
        try {
            Thread.sleep(mls);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        int mls = 1000;
        int seg = 0;
        // Rutina de conexion
        Long tiempoRutina = readJSON.getTiempoRutinaConexion();
        for (int i = 0; i < tiempoRutina; i++) {
            System.err.println("Seg: " + seg);
            if (dano) {
                enviarRutinaDano();
            } else {
                enviarRutinaConexion(Integer.toString(i));
            }
            delay(mls);
            seg++;
        }

        // Rutina normal
        tiempoRutina = readJSON.getTiempoRutina();
        while (true) {
            for (int i = 0; i < tiempoRutina; i++) {
                System.err.println("Seg: " + seg);
                if (dano) {
                    enviarRutinaDano();
                } else {
                    enviarRutinaNormal(Integer.toString(i));
                }
                delay(mls);
                seg++;
            }
        }

        // Rutina de desconexion
        /*tiempoRutina = readJSON.getTiempoRutinaDesconexion();
        for (int i = 0; i < tiempoRutina; i++) {
            enviarRutinaDesconexion(Integer.toString(i));
            delay(mls);
        }*/
    }

}
