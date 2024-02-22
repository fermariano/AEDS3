import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Musica {
    static int idCount = 0; //contador de id
    int id; //4 
    String nome;  
    String artista; 
    int popular;//4
    long date;//8
    String[] genero; //vairos generos (2)
    float dance;

    public Musica() {
        this.id = idCount++; //preenche o ID dinamicamente
        this.nome = "Vazio";
        this.artista = "Nenhum"; //nome do artista limitado
        this.popular = 0;
        this.date = 0;
        this.dance = 0;
        this.genero = iniciarGenero();
    }

    int getSizeBytes(){
           return 4 + nome.length() + artista.length() + 4 + 8 + genero[0].length() + genero[1].length() + 4;   
    }


    public Musica(String nome, String artista, int popular, String date, String genero, float dance) {
        this.id = idCount++;
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
    }

    private static String[] iniciarGenero(){
        String genero [] = new String[2];
        genero[0] = "";
        genero[1]= "";
        return genero;
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
        "\nGenero: " + genero[0] + ", " + genero[1]; 
    }

    
}
