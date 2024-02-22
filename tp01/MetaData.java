import java.io.RandomAccessFile;

public class MetaData {
    boolean valido;
    int sizeBytes;

    MetaData(){
        this.valido = false;
        this.sizeBytes = 0;
    }

    MetaData ReadMetaData(RandomAccessFile file){
        try{
            file.seek(0);
            this.valido = file.readBoolean();
            this.sizeBytes = file.readInt();
        }catch(Exception e){
            System.out.println("Erro ao ler os metadados: " + e.getMessage());
        }
        return this;
    }

    boolean ReadValidez(RandomAccessFile file){
        try{
            this.valido = file.readBoolean();
        }catch(Exception e){
            System.out.println("Erro ao ler a validez: " + e.getMessage());
        }
        return this.valido;
    }

    int ReadSizeBytes(RandomAccessFile file){ //receb o RAF com cabeçote já definido para leitura
        try{
            this.sizeBytes = file.readInt();
        }catch(Exception e){
            System.out.println("Erro ao ler o tamanho: " + e.getMessage());
        }
        return this.sizeBytes;
    }

    void WriteMetaData(RandomAccessFile file, Musica musica){ //receb o RAF com cabeçote já definido para leitura
        try{
            file.writeBoolean(true);
            file.writeInt(musica.getSizeBytes());
        }catch(Exception e){
            System.out.println("Erro ao escrever os metadados: " + e.getMessage());
        }
    }
    

    
    
}
