import java.io.RandomAccessFile;

class Bucket {
    static int maxChaves = 2;
    int profundidadeLocal;
    int chavesPresentes;
    int registroId[];
    long registroPos[];

    static RandomAccessFile raf;

    static void start() {
        try {
            raf = new RandomAccessFile("Source/DataBase/bucket.db", "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Bucket(int profundidadeLocal) {
        this.profundidadeLocal = profundidadeLocal;
        chavesPresentes = 0;
        registroId = new int[maxChaves];
        registroPos = new long[maxChaves];
        for (int i = 0; i < maxChaves; i++) {
            registroId[i] = -1;
            registroPos[i] = -1;
        }
    }

    Bucket(long posicao) {// ok
        try {
            raf.seek(posicao);
            profundidadeLocal = raf.readInt();
            chavesPresentes = raf.readInt();
            registroId = new int[maxChaves];
            registroPos = new long[maxChaves];
            for (int i = 0; i < chavesPresentes; i++) {
                registroId[i] = raf.readInt();
                registroPos[i] = raf.readLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setProfundidadeLocal(int profundidadeLocal) {
        this.profundidadeLocal = profundidadeLocal;
    }

    static int sizeBytes() {
        return 4 + 4 + 4 + maxChaves * 4 + maxChaves * 8;
    }

    public void resetBucket() {
        try {
            for (int i = 0; i < maxChaves; i++) {
                registroId[i] = -1;
                registroPos[i] = -1;
            }
            chavesPresentes = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long writeBucket() {
        try {
            raf.seek(raf.length());
            long retorno = raf.getFilePointer();
            raf.writeInt(profundidadeLocal);
            raf.writeInt(chavesPresentes);
            for (int i = 0; i < maxChaves; i++) {
                raf.writeInt(registroId[i]);
                raf.writeLong(registroPos[i]);
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    void writeBucket(long pos) {// ok
        try {
            raf.seek(pos);
            raf.writeInt(profundidadeLocal);
            raf.writeInt(chavesPresentes);
            for (int i = 0; i < maxChaves; i++) {
                raf.writeInt(registroId[i]);
                raf.writeLong(registroPos[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addChave(int id, long pos) {// ok
        if (chavesPresentes < maxChaves) {
            registroId[chavesPresentes] = id;
            registroPos[chavesPresentes] = pos;
            chavesPresentes++;
        }
    }

    public void removeChave(int id) {
        for (int i = 0; i < maxChaves; i++) {
            if (registroId[i] == id) {
                registroId[i] = -1;
                registroPos[i] = -1;
                chavesPresentes--;
                arraste(i);
                break;
            }
        }
    }

    private void arraste(int i) {
        for (int j = i; j < maxChaves - 1; j++) {
            registroId[j] = registroId[j + 1];
            registroPos[j] = registroPos[j + 1];
        }
    }

    public static void IniciarLeituraSequencial() {
        try {
            raf.seek(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bucket getBucket() throws Exception {
        try {
            if (raf.getFilePointer() < raf.length()) {
                return new Bucket(raf.getFilePointer());
            }
        } catch (Exception e) {
            if (e instanceof java.io.EOFException) {

            } else {
                throw e;
            }
        }
        return null;
    }
}

public class Diretorio {
    static int profundidadeGlobal = 1;
    static RandomAccessFile DiretorioFile;
    static RandomAccessFile BucketFile;

    public static void start() {
        try {
            boolean exists = false;
            DiretorioFile = new RandomAccessFile("Source/DataBase/diretorio.db", "rw");
            Bucket.start();
            profundidadeGlobal = 0;
            if (DiretorioFile.length() >= 4) {
                exists = true;
                profundidadeGlobal = DiretorioFile.readInt();
            }

            BucketFile = new RandomAccessFile("Source/DataBase/bucket.db", "rw");
            if (!exists) {
                Logs.Alert("sem buckets, criando...\n");
                UnitPointer[] pointers = new UnitPointer[(int) Math.pow(2, profundidadeGlobal)];
                for (int i = 0; i < Math.pow(2, profundidadeGlobal); i++) {
                    pointers[i] = new UnitPointer(i, -1);
                    pointers[i].Bucket = new Bucket(profundidadeGlobal).writeBucket();
                    writeHashPointer(i, pointers[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean hasNoBuckets() {
        try {
            return BucketFile.length() >= Bucket.sizeBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void updateProfundidade() {
        try {
            profundidadeGlobal++;
            DiretorioFile.seek(0);
            DiretorioFile.writeInt(profundidadeGlobal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int Hash(int id) {
        return id % (int) Math.pow(2, profundidadeGlobal);
    }

    public static void inserir(MetaIndice reg) {
        if (reg == null)
            return;

        int hash = Hash(reg.getId()); // calcular o hash
        UnitPointer BucketP = getHashPointer(hash); // ler o ponteiro do bucket
        Bucket bucket = new Bucket(BucketP.Bucket);// ler o bucket do ponteiro
        if (bucket.chavesPresentes < Bucket.maxChaves) { // se o bucket não estiver cheio
            bucket.addChave(reg.getId(), reg.getPosicao());
            bucket.writeBucket(BucketP.Bucket);

        } else {// se o bucket estiver cheio
            splitBucket(BucketP, bucket, reg);
        }
    }

    public static boolean searchID(int id) {
        int hash = Hash(id);
        UnitPointer BucketP = getHashPointer(hash);
        Bucket bucket = new Bucket(BucketP.Bucket);
        for (int i = 0; i < Bucket.maxChaves; i++) {
            if (bucket.registroId[i] == id) {
                System.out.println("indice enctronado");
                return true;
            }
        }
        System.out.println("indice não encontrado");
        return false;
    }

    public static void splitBucket(UnitPointer fullBucketP, Bucket fullBucket, MetaIndice reg) {
        MetaIndice[] chaves = new MetaIndice[Bucket.maxChaves + 1];

        if (fullBucket.profundidadeLocal == profundidadeGlobal) { // se o bucket estiver no nivel maximo
            duplicarDiretorio();
        }

        for (int i = 0; i < Bucket.maxChaves; i++) { // copia as chaves do bucket
            chaves[i] = new MetaIndice(fullBucket.registroId[i], fullBucket.registroPos[i]);
        }
        chaves[Bucket.maxChaves] = reg; // adiciona a nova chave

        fullBucket.resetBucket(); // reseta o bucket
        fullBucket.writeBucket(fullBucketP.Bucket); // escreve o bucket agora vazio

        for (MetaIndice chave : chaves) {
            int hash = Hash(chave.getId());
            UnitPointer BucketP = getHashPointer(hash);
            Bucket bucket = new Bucket(BucketP.Bucket);

            if (bucket.profundidadeLocal != profundidadeGlobal) {// 2 ponteiros em um bucket
                bucket = new Bucket(profundidadeGlobal); // cria novo Bucket
                BucketP.Bucket = bucket.writeBucket(); // cria um novo bucket o ponteiro
                fullBucket.profundidadeLocal++;
                fullBucket.writeBucket(fullBucketP.Bucket);

                writeHashPointer(hash, BucketP);
            }
            if (bucket.chavesPresentes < Bucket.maxChaves) {
                bucket.addChave(chave.getId(), chave.getPosicao());
                bucket.writeBucket(BucketP.Bucket);
            } else {
                splitBucket(BucketP, bucket, chave);
            }
        }
    }

    static void duplicarDiretorio() {
        UnitPointer novosBuckets[] = new UnitPointer[(int) Math.pow(2, profundidadeGlobal)];
        for (int i = 0; i < Math.pow(2, profundidadeGlobal); i++) {// expande diretorio e faz apontar para os antigos
            UnitPointer buckeP = getHashPointer(i);
            novosBuckets[i] = new UnitPointer(buckeP.Bucket);
            novosBuckets[i].numero = i + (int) Math.pow(2, profundidadeGlobal);
            writeHashPointer(novosBuckets[i].numero, novosBuckets[i]);
        }
        updateProfundidade();
    }

    public static UnitPointer getHashPointer(int hash) {// por enquanto ok
        try {
            DiretorioFile.seek(4 + (hash + 1) * UnitPointer.sizeBytes - UnitPointer.sizeBytes);
            // ta funcionando não vou tocar

            return new UnitPointer(DiretorioFile.readInt(), DiretorioFile.readLong());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeHashPointer(int hash, UnitPointer pointer) {
        try {
            DiretorioFile.seek(4 + (hash + 1) * UnitPointer.sizeBytes - UnitPointer.sizeBytes);
            DiretorioFile.writeInt(pointer.numero);
            DiretorioFile.writeLong(pointer.Bucket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PrintAllBuckets() {
        try {
            int count = 0;
            for (int i = 0; i < Math.pow(2, profundidadeGlobal); i++) {
                UnitPointer BucketP = getHashPointer(i);
                Bucket bucket = new Bucket(BucketP.Bucket);
                System.out.println("Bucket: " + i);
                for (int j = 0; j < Bucket.maxChaves; j++) {
                    if (bucket.registroId[j] != -1 && bucket.registroId[j] != 0) {
                        System.out.println("Chave: " + bucket.registroId[j] + " Pos: " + bucket.registroPos[j]);
                        count++;
                    }
                }
            }
            System.out.println("Chaves: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean search(int id){
        int hash = Hash(id);
        UnitPointer BucketP = getHashPointer(hash);
        Bucket bucket = new Bucket(BucketP.Bucket);
        for (int i = 0; i < Bucket.maxChaves; i++) {
            if (bucket.registroId[i] == id) {
                return true;
            }
        }
        return false;
    }

    public static class UnitPointer { // classe que representa um ponteiro para um bucket
        int numero;
        long Bucket;

        final static int sizeBytes = 4 + 8;

        public UnitPointer(int numero, long BucketPos) {
            this.Bucket = BucketPos;
            this.numero = numero;
        }

        UnitPointer(long BucketPos) {
            this.Bucket = BucketPos;
        }
    }
}