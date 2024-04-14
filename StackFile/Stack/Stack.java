package Stack;
import java.nio.ByteBuffer;
import java.util.Random;
import java.io.RandomAccessFile;

class Node {
    int data;
    long next;
    Node(int value) {
        this.data = value;
        this.next = -1;
    }
    Node(int value, long next) {
        this.data = value;
        this.next = next;
    }
    public byte[] toByteArray() {
        int size = 4 + 8; // 4 bytes para o int e 8 bytes para o long
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(data);
        buffer.putLong(next);
        return buffer.array();
    }
}

class Stack {//crescde de cima para baixo, topo aponta para o ultimo elemento inserido
    int size; // quantidade de elementos na pilha
    long top; // posição do topo da pilha
    RandomAccessFile file;

    public Stack(String filename) {
        this.size = 0;
        this.top = -1;
        try {
            this.file = new RandomAccessFile(filename, "rw");
    
            if (this.file.length() < 12 ) {
                this.file.writeInt(this.size);
                this.file.writeLong(this.top);
            } else {
                this.file.seek(0);
                this.size = this.file.readInt();
                this.top = this.file.readLong();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(int value) {
        Node node = new Node(value);
        long position = FileMalloc(node);
        if (this.size == 0) {
            this.top = position;
        } else {
            UpdatePointer(position, this.top);
            this.top = position;
        }
        this.size++;
        UpdateStack(node);
    }

    public void pop(){
        if (this.size == 0) {
            System.out.println("Stack is empty");
            return;
        }
        Node node = readNode(this.top);
        this.top = node.next;
        this.size--;
        UpdateStack(node);
    }

    public long FileMalloc(Node node) {
        try {
            long position = this.file.length();
            this.file.seek(position);
            this.file.write(node.toByteArray());
            return position; //literalmente um malloc
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return -1;
    }


    public void UpdatePointer(long position, long next) {
        try {
            this.file.seek(position + 4); //pula para o ponteiro
            this.file.writeLong(next);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateStack(Node node) {
        try {
            this.file.seek(0);
            this.file.writeInt(this.size);
            this.file.writeLong(this.top);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Node readNode(long NodePos) {
        try {
            this.file.seek(NodePos);
            byte[] data = new byte[12];
            this.file.read(data);
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int value = buffer.getInt();
            long next = buffer.getLong();
            Node node = new Node(value, next);
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printStackFile(RandomAccessFile file) {
        try {
            long pointer = this.top; // Começa do topo
            while (pointer != -1) {
                Node node = readNode(pointer);
                
                // Escreve os atributos do nó no arquivo
                file.writeBytes("┌───────────┐\n");
                file.writeBytes("│  Data: " + String.format("%3d", node.data) + "  │\n");
                file.writeBytes("│  Next: " + String.format("%3d", node.next) + "  │\n");
                file.writeBytes("└───────────┘\n");
    
                // Desenha seta para o próximo nó, se houver
                if (node.next != -1) {
                    file.writeBytes("     |||\n");
                    file.writeBytes("     |│|\n");
                    file.writeBytes("     \\|/\n");
                }
    
                pointer = node.next; // Avança para o próximo nó
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStack() {
        long pointer = this.top; // Começa do topo
        while (pointer != -1) {
            Node node = readNode(pointer);
            System.out.println("Data: " + node.data + " Next: " + node.next);
            pointer = node.next; // Avança para o próximo nó
        }
    }
    
    

    public static void main(String[] args)throws Exception{
        Stack stack = new Stack("stack.bd");
        Random rand = new Random();
        for( int i = 0; i<10; i++){
            stack.push(rand.nextInt(1000));
        }

        
        RandomAccessFile file = new RandomAccessFile("stack.txt", "rw");        
        stack.printStackFile(file);
        stack.printStack(); 

    }

}