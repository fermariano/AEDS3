
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Musica {
    private static int LastID = 0;
    int id; //4 
    String nome;  
    String artista; 
    int popular;//4
    long date;//8
    String []genero; //vairos generos (2)
    float dance;
    String hash;

    //getter
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getArtista() {
        return artista;
    }
    public int getPopularidade() {
        return popular;
    }
    public String getDataLancamento() {
        return timestampConv(date);
    }
    public String getGenero() {
        return genero[0] + ", " + genero[1];
    }
    public float getDancabilidade() {
        return dance;
    }
    public String getHash() {
        return hash;
    }
    
    public Musica() {
        this.nome = "Vazio";
        this.artista = "Nenhum"; //nome do artista limitado
        this.popular = 0;
        this.date = 0;
        this.dance = 0;
        this.genero = iniciarGenero();
        this.hash = "";
    }

 



    public Musica(String nome, String artista, int popular, String date, String genero, float dance, String hash) {
        this.nome = nome;
        this.artista = artista;
        this.popular = popular;
        try {
            this.date = dataConverter(date);
        } catch (Exception e) {
            try{
                this.date = dataConverter(date+"-11-22"); //<3
            }catch(Exception e2){ //desisto
                this.date = dataConverter(date+"-22");
            }
            
        }
        this.genero = genero.split(";");
        this.dance = dance;
        this.hash = hash;
    }

    public Musica(int id, String nome, String artista, int popular, long date, String genero, float dance, String hash) {
        this.id = id;
        this.nome = nome;
        this.artista = artista;
        this.popular = popular;
        this.date = date;
        this.genero = genero.split(";");
        this.dance = dance;
        this.hash = hash;
    }

    private static String[] iniciarGenero(){
        String genero [] = new String[2];
        genero[0] = "";
        genero[1]= "";
        return genero;
    } 

    public static int getLastID(){
        return LastID;
    }

    public static void setLastID(int id){
        LastID = id;
    }

    // converte a data para timestamp
    private long dataConverter(String data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // define o formato da data
        LocalDate localDate = LocalDate.parse(data, formatter); // converte string para localdate
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant(); // converte localdate para instant
        return instant.toEpochMilli(); // retorna timestamp
    }

    // converte timestamp para data
    private String timestampConv(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp); // converte timestamp para instant
        
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate(); // converte instant para localdate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // formata a data
        return localDate.format(formatter);
    }

    public String toString() {
        return "\nID: " + id +
        "\nNome: " + nome +
        "\nArtista: " + artista + 
        "\nPopular: " + popular + 
        "\nDate: " + timestampConv(date) + 
        "\nDance: " + dance +
        "\nGenero: " + genero[0] + ", " + genero[1] + 
        "\nHash: " + hash;
    }

    

    public static Musica StringToMusica(String str){
        String[] atributos = str.split(",");

        if(atributos.length ==7){//construir sem ID
            return new Musica(atributos[0],atributos[1],Integer.parseInt(atributos[2]),atributos[3],atributos[4],Float.parseFloat(atributos[5]),atributos[6]);
            
        }else if(atributos.length == 8){ //construir com ID
            return new Musica(Integer.parseInt(atributos[0]),atributos[1],atributos[2],Integer.parseInt(atributos[3]),Long.parseLong(atributos[4]),atributos[5],Float.parseFloat(atributos[6]),atributos[7]);
        }
        return null;
        

    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreva os campos na ordem correta
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(artista);
        dos.writeInt(popular);
        dos.writeLong(date);
        String newgenero = genero[0] + ";" + genero[1];
        dos.writeUTF(newgenero);
        dos.writeFloat(dance);
        dos.writeUTF(hash);

        dos.close(); // Feche o DataOutputStream
        return baos.toByteArray(); // Retorne o array de bytes
    }

    public static Musica fromByteArray(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);
    
            // Leia os campos na ordem correta
            int id = dis.readInt();
            String nome = dis.readUTF();
            String artista = dis.readUTF();
            int popular = dis.readInt();
            long date = dis.readLong();
            String genero = dis.readUTF();
            float dance = dis.readFloat();
            String hash = dis.readUTF();
    
            dis.close(); // Feche o DataInputStream
            return new Musica(id, nome, artista, popular, date, genero, dance, hash);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Ou lançar uma exceção personalizada, dependendo do seu caso de uso
        }
    }

    
}