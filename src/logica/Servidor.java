package logica;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private ServerSocket server;
    private Socket cliente;
    private int puerto;
    private boolean conectarActivo;

    public Servidor() {
        puerto = 5000;
        conectarActivo = true;
    }

    public void conectar() {
        try {
            // Crear el servidor
            server = new ServerSocket(puerto);
            Runnable hiloRecepcionClientes = () -> {
                while(conectarActivo){
                    //Esperar a que alguien se conecte
                    try {
                        cliente = server.accept();
                    } catch (IOException e) {
                        System.err.println("Error aceptando el cliente");
                        throw new RuntimeException(e);
                    }
                    // Alguien se conectó
                    InterseccionHilo c = new InterseccionHilo(cliente);
                    c.start();
                }
            };
            new Thread(hiloRecepcionClientes).start();
        } catch (IOException e) {
            System.err.println("Error en el flujo del servidor");
            desconectar();
            throw new RuntimeException(e);
        }
    }

    public void desconectar() {
        try {
            server.close();
            System.out.println("Conexion terminada");
        } catch (IOException e) {
            System.out.println("Error en la desconexion");
            throw new RuntimeException(e);
        }
    }
}
