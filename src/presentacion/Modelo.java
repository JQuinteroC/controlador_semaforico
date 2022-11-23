package presentacion;

import logica.Servidor;

import java.io.IOException;

public class Modelo {

    private Servidor servidor;

    public Servidor getMiSistema() {
        if(servidor == null){
            servidor = new Servidor();
        }
        return servidor;
    }

    public void iniciar() {
        System.out.println("Estableciendo conexión...");
        getMiSistema().conectar();
    }
}
