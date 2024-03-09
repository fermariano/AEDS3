use std::io::Read;



fn main() {
  //loops for and while
//for
    let mut count = 0;
    for i in 0..10
    {
        print!("i = {}\n", i);
        count +=1;
    }
    print!("count = {}\n", count);

    //while
    let mut i = 0;
    while i < 10
    {
        print!("i = {}\n", i);
        i += 1;
    }
    //loop
    i = 0;
    loop
    {
        print!("i = {}\n", i);
        i += 1;
        if i == 10
        {
            break;
        }
    }
    //loop com break e continue
    
    
}
