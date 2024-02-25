
//metodos de escrita e leitura aqui 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;

public class Arq {
    protected static boolean status = false; // se esta em operação
    protected static String file; // nome do arquivo
    protected static RandomAccessFile raf;
    protected static DataOutputStream dos;
    protected static DataInputStream dis;
    protected static MetaData meta;

    public static void Iniciar(String filex) {
        file = filex;
        try {
            raf = new RandomAccessFile(file, "rw");
            status = true;
            meta = new MetaData(raf);
            Musica.setLastID(raf.readInt());
        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

    }

    public static boolean getStatus() {
        return status;
    }

    public static void UpdateSong(int ID, String newSong) {
        try {
            IniciarLeituraSequencial();

            int idSeacher = -1;
            long seekSaver = 0;
            long metaSeek = 0;

            for (; idSeacher != ID || meta.lapide; raf.seek(raf.getFilePointer() + (meta.sizeBytes - 4))) { // pula os
                                                                                                            // bytes de
                // registro ate achar o ID
                metaSeek = raf.getFilePointer(); // salva a posição do inicio da lapide
                meta.readMetaData(); // le os metadados
                seekSaver = raf.getFilePointer(); // salva a posição do inicio do registro
                idSeacher = raf.readInt(); // le o ID
            }

            // achou ID
            raf.seek(seekSaver); // retorna a posição do inicio do registro
            Musica nova = Musica.StringToMusica(newSong); // cria um novo objeto com a string de parametro
            int novaSize = nova.toByteArray().length;

            if (novaSize <= meta.sizeBytes) { // pode sobrescrever
                raf.seek(seekSaver);
                raf.write(nova.toByteArray());
                // volta para o inicio do registro (lapide)
            } else {
                raf.seek(metaSeek); // volta para o inicio do registro (lapide)
                raf.writeBoolean(true); // marca como lapide
                addRegistroExistenteEOF(nova);// update em fim de arquivo sem alterar ultimo ID
            }

        } catch (IOException e) { // raf buscou ID até não encontrar
            System.out.println("Musica não Encontrada" + "ou" + e.getMessage());
        }
    }

    public static void IniciarLeituraSequencial() {
        try {
            raf.seek(4);// pula o lastID
        } catch (IOException e) {
            System.out.println("Erro ao iniciar a leitura sequencial: " + e.getMessage());
        }
    }

    public static Musica getRegistro() {
        try {
            Musica buffer = new Musica();
            byte[] bytes;
            do {
                meta.readMetaData();
                bytes = new byte[meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(bytes); // Lê o registro completo
            } while (meta.lapide && raf.getFilePointer() < raf.length());

            buffer = Musica.fromByteArray(bytes);
            return buffer;
        } catch (IOException e) {
            
            System.out.println("Arquivo chegou ao fim" + e.getMessage());
            return null;
        }

    }

    public Musica getSongFromRafNow() {
        try {
            Musica buffer = new Musica();
            meta.readMetaData();
            byte[] ba = new byte[meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
            raf.readFully(ba); // Lê o registro completo
            buffer = Musica.fromByteArray(ba); // Converte para objeto Musica
            return buffer;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean addRegistro(String str) {
        byte[] ba;
        try {
            ba = Musica.StringToMusica(str).toByteArray(); // atualiza lastID na classe mas não no arquivo
            Musica.setLastID(Musica.getLastID() + 1); // atualiza na classe
            writeLastID(Musica.getLastID()); // atualiza no arquivo
            raf.seek(raf.length());
            meta.writeMetaData(ba.length);
            raf.write(ba);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean addRegistroExistenteEOF(Musica newsong) {
        try {
            byte[] ba = newsong.toByteArray();
            raf.seek(raf.length());
            meta.writeMetaData(ba.length);
            raf.write(ba);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static int ReadLastID() {
        try {
            raf.seek(0);
            return raf.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler o último ID: " + e.getMessage());
            return -1;
        }

    }

    public static void writeLastID(int id) {
        try {
            raf.seek(0);
            raf.writeInt(id);
        } catch (IOException e) {
            System.out.println("Erro ao escrever o último ID: " + e.getMessage());
        }
    }

    public static void Finalizar() {
        status = false;
        try {
            raf.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
        }

    }

}
