use std::io::Read;

fn main() {
    let mut bool_var: bool = true;
    //formas de condicionais

    if bool_var {
        print!("bool_var é true\n");
    } else {
        print!("bool_var é false\n");
    }

    bool_var = false;

    if !bool_var {
        //não requer ( ) para a condição
        print!("bool_var é false\n");
    } else {
        print!("bool_var é true\n");
    }

    let mut char_var: char = 'b';

    match char_var //assemelha-se ao switch case
    {
       'a' => print!("char_var é a\n"),
       'b' => print!("char_var é b\n"),
       'c' => print!("char_var é c\n"),
       _ => print!("char_var é outra coisa\n"),
    }

    let number = 10;

    match number {
        num if num % 2 == 0 => println!("{} é par", number),
        _ => println!("{} é ímpar", number),
    }
}
