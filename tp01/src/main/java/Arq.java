//metodos de escrita e leitura aqui 
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;


public class Arq {
    protected static boolean status = false; //se esta em operação
    protected static String file; //nome do arquivo
    protected static RandomAccessFile raf;
    protected static DataOutputStream dos;
    protected static DataInputStream dis;
    protected static MetaData meta;



    public static void Iniciar(String file){
        file = file;
        try {
            raf = new RandomAccessFile(file, "rw");
            status = true;
            meta = new MetaData(raf);
            Musica.setLastID(raf.readInt());
        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }
        
    }


    public static void PrintarRegistros(){
        try {
            raf.seek(0); // Vai para o início do arquivo
            Musica teste = new Musica(); // Cria o objeto que vai ser usado para ler os registros
            int idReg = raf.readInt(); // Lê o último ID adicionado
            System.out.println("Ultimo ID adicionado: " + idReg);
            while (raf.getFilePointer() < raf.length()) { // Enquanto houver registros no arquivo
                meta.readMetaData(); // Lê os metadados
                byte[] ba = new byte[meta.sizeBytes]; // Cria um array de bytes com o tamanho do registro
                raf.readFully(ba); // Lê o registro completo
                teste = Musica.fromByteArray(ba); // Converte para objeto Musica
                System.out.println(teste);
                System.out.println(meta);
            }
            System.out.println("idRe =" + idReg);
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean addRegistro(String str) {
        byte[] ba;
        try {
           
            
            ba = Musica.criarObjeto(str).toByteArray(); //atualiza lastID na classe mas não no arquivo
            writeLastID(Musica.getLastID()); //atualiza no arquivo
            raf.seek(raf.length());
            meta.writeMetaData(ba);
            raf.write(ba);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }

    } 

    public static int ReadLastID(){
        try {
            raf.seek(0);
            return raf.readInt();
        } catch (IOException e) {
            System.out.println("Erro ao ler o último ID: " + e.getMessage());
            return -1;
        }
    
    }

    public static void writeLastID(int id){
        try {
            raf.seek(0);
            raf.writeInt(id);
        } catch (IOException e) {
            System.out.println("Erro ao escrever o último ID: " + e.getMessage());
        }
    }




    public static void Finalizar(){
        status = false;
        try {
            raf.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
        }

    }

}
