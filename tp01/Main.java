import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;


public class Main {

    public static String[] lerArq(String path) {
        File file = new File(path); // le o arquivo
        String[] arrData = new String[33000]; // array com linha por linha do arquivo
        int i = 0; 
        try {
            Scanner scanner = new Scanner(file); 
            scanner.nextLine(); 
            while (scanner.hasNext()) {
                arrData[i] = scanner.nextLine(); // le linha por linha e salva no array
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
        return arrData;
    }
    


    public static void main(String[] args) {

        String[] arrData = lerArq("spotSongs.csv"); // le o arquivo e cada posiçao é uma linha
        Musica[] musicaData = new Musica[arrData.length]; // array com todas as musicas
        
        for (int i = 0; i < arrData.length && arrData[i] != null; i++) {
    
                String[] atributos = arrData[i].split(",");
                System.out.println("------");
                for (String a : atributos) System.out.println(a);
                    System.out.println("------");
                    musicaData[i] = new Musica(atributos[0], atributos[1], Integer.parseInt(atributos[2]), atributos[3], atributos[4], Float.parseFloat(atributos[5]), atributos[6]);
            
            }
        

        FileOutputStream arq;
        FileInputStream arq2;
        DataOutputStream dos;
        DataInputStream dis;
        byte[] ba;



        MetaData meta;

            
        try {

            // escrita
            arq = new FileOutputStream("songs.db");
            dos = new DataOutputStream(arq);
            
            for (int i = 0; i < musicaData.length && musicaData[i]!= null; i++) {
                meta = new MetaData();
                
                ba = musicaData[i].toByteArray();
                dos.writeInt(ba.length);
                dos.write(ba);
                
            }

            arq.close();

            // leitura

            Musica teste = new Musica();

            arq2 = new FileInputStream("songs.db");
            dis = new DataInputStream(arq2);
            int tam;

            while (dis.available() > 0) {
                tam = dis.readInt();
                ba = new byte[tam];
                dis.read(ba);
                teste.fromByteArray(ba);
                System.out.println(teste);
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
