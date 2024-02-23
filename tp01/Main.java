import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {

    // função que le o arquivo CSV
    public static String[] lerCSV(String path) {
        File file = new File(path); // le o arquivo
        String[] arrData = new String[32254]; // array com linha por linha do arquivo
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

    
    public static void lerFile(){
        
    }
    
    
    public static void main(String[] args) {

        String[] arrData = lerCSV("spotSongs.csv"); // le o arquivo e cada posiçao é uma linha
        Musica[] musicaData = new Musica[arrData.length]; // array com todas as musicas

        for (int i = 0; i < arrData.length && arrData[i] != null; i++) {
            String[] atributos = arrData[i].split(","); // da split em cada virgula para pegar os atributos
            // cria o objeto
            musicaData[i] = new Musica(atributos[0], atributos[1], Integer.parseInt(atributos[2]), atributos[3],
                    atributos[4], Float.parseFloat(atributos[5]), atributos[6]);
        }

        // classes que serao utilizadas para leitura/escrita
        FileOutputStream arq;
        FileInputStream arq2;
        DataOutputStream dos;
        DataInputStream dis;
        byte[] ba;

        MetaData meta = new MetaData();

        try {

            // escrita
            arq = new FileOutputStream("songs.db");
            dos = new DataOutputStream(arq);
            dos.writeInt(musicaData[musicaData.length - 1].id); //
            for (int i = 0; i < musicaData.length && musicaData[i] != null; i++) {
                ba = musicaData[i].toByteArray(); // transforma o objeto em array de bytes
                meta.writeMetaData(dos, ba);
                dos.write(ba); // grava o registro
            }

            arq.close(); // fecha o arquivo

            // leitura
            Musica teste = new Musica(); // cria o objeto que vai ser salvo os registros

            arq2 = new FileInputStream("songs.db");
            dis = new DataInputStream(arq2);
            

            int idReg = dis.readInt();

            while (dis.available() > 0) { // enquanto tiver registros no arquivo
                meta.readMetaData(dis); // le os metadados
                 ba = new byte[meta.sizeBytes]; // cria um array de bytes com o tam do registro
                dis.read(ba); // le o registro
                teste = Musica.fromByteArray(ba); // transforma para string
                System.out.println(teste);
                System.out.println(meta);   
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
