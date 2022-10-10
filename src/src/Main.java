package src;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public  class Main{

    public static void main(String[]args) {
        try {
            new Servidor().conectar();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
