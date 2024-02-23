import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;


public class Main {

    // função que le o arquivo CSV
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
        int cont = 0;
        
        for (int i = 0; i < arrData.length && arrData[i] != null; i++) {
                String[] atributos = arrData[i].split(","); // da split em cada virgula para pegar os atributos
                    // cria o objeto
                    musicaData[i] = new Musica(atributos[0], atributos[1], Integer.parseInt(atributos[2]), atributos[3], atributos[4], Float.parseFloat(atributos[5]), atributos[6]);
                    cont++;
            }

        
        

        // classes que serao utilizadas para leitura/escrita
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
                
                ba = musicaData[i].toByteArray(); // transforma o objeto em array de bytes
                dos.writeInt(ba.length); // grava o tamanho em bytes do registro
                dos.write(ba); // grava o registro
                
            }

            arq.close(); // fecha o arquivo

            // leitura

            Musica teste = new Musica(); // cria o objeto que vai ser salvo os registros

            arq2 = new FileInputStream("songs.db");
            dis = new DataInputStream(arq2);
            int tam; // variavel para delimitar o tamanho do registro

            while (dis.available() > 0) { // enquanto tiver registros no arquivo
                tam = dis.readInt(); // le o tamanho do registro

                    ba = new byte[tam]; // cria um array de bytes com o tam do registro
                    dis.read(ba); // le o registro
                    teste.fromByteArray(ba); // transforma para string
                    System.out.println(teste); // printa
                     
                
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
