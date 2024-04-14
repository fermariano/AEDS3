import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

class Node{
    long esq;
    long dir;
    int value;

    public Node(int value) {
        this.value = value;
        this.esq = -1;
        this.dir = -1;
    }
    public static byte[] toByteArray(Node node) {
        ByteBuffer buffer = ByteBuffer.allocate(20);// 8 bytes para o long e 4 bytes para o int
        buffer.putLong(node.esq);
        buffer.putLong(node.dir);
        buffer.putInt(node.value);
        return buffer.array();
    }

    public static Node fromByteArray(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        Node node = new Node(0);
        node.esq = buffer.getLong();
        node.dir = buffer.getLong();
        node.value = buffer.getInt();
        return node;
    }
}

public class BinaryTree {
   
    RandomAccessFile file;
    long root;
    int size;

    public BinaryTree(String filename) {
        this.size = 0;
        this.root = -1;
        try {
            this.file = new RandomAccessFile(filename, "rw");
            if (this.file.length() < 12) { //1 long e 1 int 
                this.file.writeInt(this.size);
                this.file.writeLong(this.root);
            } else {
                this.file.seek(0);
                this.size = this.file.readInt();
                this.root = this.file.readLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long FileMalloc(Node node) {
        try {
            long position = this.file.length();
            this.file.seek(position);
            this.file.write(Node.toByteArray(node));
            return position;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void UpdateTree(long root, int size){
        try {
            this.file.seek(0);
            this.file.writeInt(size);
            this.file.writeLong(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(int value) {
        Node node = new Node(value);
        if (this.root == -1) {
            this.root = FileMalloc(node);
            UpdateTree(this.root, this.size);
        } else {
            insert(this.root, node);
        }
        this.size++;
    }

    private void insert(long TempNode, Node node) {
        try {
            this.file.seek(TempNode);
            byte[] buffer = new byte[20];
            this.file.read(buffer);
            Node actual = Node.fromByteArray(buffer);
            if (node.value < actual.value) {
                if (actual.esq == -1) {//adiciona a esquerda
                    this.file.seek(TempNode);
                    long newNodepos = FileMalloc(node);
                    setEsq(TempNode, newNodepos);
                } else {
                    insert(actual.esq, node);
                }
            } else {
                if (actual.dir == -1) {
                    this.file.seek(TempNode);
                    long newNodepos = FileMalloc(node);
                    setDir(TempNode, newNodepos);
                } else {
                    insert(actual.dir, node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    void setEsq(long position, long esq) {
        try {
            this.file.seek(position);
            this.file.writeLong(esq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDir(long position, long dir) {
        try {
            this.file.seek(position + 8);
            this.file.writeLong(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("=====================Binary Tree=====================");
        System.out.println("\t==**Root: " + this.root+"**==");
        print(this.root, 1);
    }

    private void print(long position, int level) {
        try {
            this.file.seek(position);
            byte[] buffer = new byte[20];
            this.file.read(buffer);
            Node node = Node.fromByteArray(buffer);
            
            // Imprimir detalhes do nó
            String indentation = "\t".repeat(level); // Usar tabulação para aninhar a saída
            System.out.println(indentation + "Value: " + node.value);
            System.out.println(indentation + "Position: " + position);
            System.out.println(indentation + "Left Child Position: " + node.esq);
            System.out.println(indentation + "Right Child Position: " + node.dir);
            System.out.println();
            
            // Recursivamente imprimir os filhos
            if (node.esq != -1) {
                System.out.println(indentation + "Left Child:");
                print(node.esq, level + 1);
            }
            if (node.dir != -1) {
                System.out.println(indentation + "Right Child:");
                print(node.dir, level + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree("arvore.bin");
        tree.print();
    }
}
