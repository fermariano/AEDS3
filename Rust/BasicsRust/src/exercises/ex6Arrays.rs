use core::slice;
use std::{array, io::Read};

fn print_array(array: &[i32]){
    //array[0] = 0; //não posso fazer isso pois o array é imutavel
    for i in array{
        print!("{} ", i);
    }
    print!("\n");
}

fn selection_sort(array: &mut [i32]){
    array[0] = 10; //posso fazer isso pois o array é mutavel

    let size = array.len();

    for i in 0..size-1{
        let mut min = array[i];
        for j in i+1..size{
            if array[j] < min{
                min = array[j];
                array[j] = array[i];
                array[i] = min;
            }
        }
    }
    print_array(array);
    
}

fn process_array(array: &[i32]){ //recebe referencia para array 
//não pode ser mutavel e não possui propriedade de ownership

    for i in array{
        print!("{} ", i);
    }
    //array[0] = 10; //não posso fazer isso pois o array é imutavel

}


fn get_ownership(mut array: [i32;10]){ //copia o array para a função / não é uma referencia
    //aqui o array é passado como parametro e o ownership é transferido para a função
     // array = [1,2]; //não posso fazer isso pois o tamanho é fixo como propriedade do array
    for i in 0..10{
        array[i] = i as i32;
    }
    println!("Array dentro da função: ");
    print_array(&array);
} 

fn main() {
    //Arrays


    let mut array_inferido:[i32;10] = [1,4,2,3,7,5,6,9,10,8];
    let mut array_zerado: [i32; 10] = [0; 10]; //inicializa um array de 10 posições com 0
    let array_imutavel: [i32; 5] = [1,2,3,4,5];

    //slices são uma referência a uma seção de array,ou seja, um ponteiro para o primeiro elemento e um tamanho
    print_array(&array_imutavel);//borrowing do array imutavel
    selection_sort(&mut array_inferido); // 
                    // para não ter q passar o ownership do array,
                    //passo ele como slice, que é uma referência ao array
                    //como deve ordenar, passo como slice mutavel

    // selection_sort(&mut array_imutavel); //não posso fazer isso pois o array é imutavel


    slices_show();
    process_array(&array_inferido); //não pode ser mutado
    get_ownership(array_zerado); // <- copia o array para a função


    println!("Array zerado: ");
    print_array(&array_zerado); // -> array ainda zerado

}



fn slices_show(){
 // Definindo um array de inteiros
 let array: [i32; 5] = [1, 2, 3, 4, 5];

 // Definindo um slice a partir do array
 let slice_3 = &array[0..3]; // Slice que contém os elementos do índice 0 ao 2 ( 3 elementos)
 //posso fazer um slice de tamanho real do array também

 // Exibindo os elementos do array
 println!("Array:");
 for element in &array {
     print!("{} ", element);
 }
 println!(); // Nova linha para separar os elementos do array e do slice

 // Exibindo os elementos do slice
 println!("Slice:");
 for element in slice_3 {
     print!("{} ", element);
 }
 println!(); // Nova linha para separar os elementos do array e do slice

 // Exibindo a diferença de tamanho entre o array e o slice
 println!("Tamanho do array: {}", array.len());
 println!("Tamanho do slice: {}", slice_3.len());

 //se eu tentar acessar um elemento fora do tamanho do slice, o programa vai dar erro
 for i in [0..5]{
    if let Some(element) = slice_3.get(i) {
        print!("{:?} ", element);
    } else {
        print!("Índice fora dos limites.\n ");
        continue;
    }


 }
}
