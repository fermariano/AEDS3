use std::io::Read;



fn main() {
    let mut input = String::new();
    let scanner = std::io::stdin();

    scanner.read_line(&mut input).unwrap(); //le o \n também, então a String input tem um \n no final
    input.pop(); //remove o \n do final da string
    print!("Hello, {}!", input);
   

}
