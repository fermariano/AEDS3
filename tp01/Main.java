import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public static void gravarFile(Musica[] musicaData, DataOutputStream dos) throws IOException {
        
        byte[] ba;

        MetaData meta = new MetaData();

            dos.writeInt(musicaData[musicaData.length - 1].id); //
            for (int i = 0; i < musicaData.length && musicaData[i] != null; i++) {
                ba = musicaData[i].toByteArray(); // transforma o objeto em array de bytes
                meta.writeMetaData(dos, ba); // grava os metadados do registro
                dos.write(ba); // grava o registro
            }

           // arq.close(); // fecha o arquivo

    }

    public static Musica criarObjeto(String str) {
        String[] atributos = str.split(",");

        Musica objeto = new Musica(atributos[0], atributos[1], Integer.parseInt(atributos[2]), atributos[3],
        atributos[4], Float.parseFloat(atributos[5]), atributos[6]);

        return objeto;
    }

    // falta colocar o ultimo id adicionado!
    public static boolean addRegistro(String str, DataOutputStream dos) {
        byte[] ba;

        MetaData meta = new MetaData();

        try {
            ba = criarObjeto(str).toByteArray();
            meta.writeMetaData(dos, ba);
            dos.write(ba);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

    } 

    
    public static void lerFile() throws IOException{
        FileInputStream arq2;
        DataInputStream dis;
        byte[] ba;
        MetaData meta = new MetaData();

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
        

        try {
            FileOutputStream arq = new FileOutputStream("songs.db");
            DataOutputStream dos = new DataOutputStream(arq);
            gravarFile(musicaData, dos);
          //  lerFile(); // le todo o arquivo

          int option = -1;
          Scanner teclado = new Scanner(System.in);

            do {
                System.out.println("Menu do CRUD");
                System.out.println("1- Adicionar registro");
                System.out.println("2- Atualizar registro");
                System.out.println("3- Pesquisar registro");
                System.out.println("4- Deletar registro");
                System.out.println("5- Encerrar programa");
                option = teclado.nextInt();

                switch (option) {
                    case 1:
                    teclado.nextLine();
                    System.out.println("Digite os atributos em ordem e separados por vírgula: nome (string), artista (string), nível de popularidade (int), data de lançamento (yyyy-MM-dd), genero (string), dançabilidade (float), hash (string)");
                    String atributo = teclado.nextLine();
                    addRegistro(atributo, dos);
                    break;
                    case 2:
                    // att o registro
                    break;
                    case 3:
                    // pesquisa o registro
                    break;
                    case 4:
                    // deleta o registro
                    break;
                    default:
                    // nada
                }

                teclado.close();

            } while (option != 5);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        

        try {
            lerFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        

    }
}
