import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import javax.sound.midi.Soundbank;


public class Main {

    
   

    // falta colocar o ultimo id adicionado!
   


    
    
    public static void main(String[] args) {
        Arq.Iniciar("songs.db");
        Arq.addRegistro("Only For You - Maor Levi Remix,Mat Zo,15,2014-01-01,edm;progressive electro house,626,4MgHuBKdGuS4QUTb7HvPRG");
        
        Arq.PrintarRegistros();
        
        System.out.println("ultimo ID inserido foi: " + Musica.getLastID());

        Arq.Finalizar();

    }
}
