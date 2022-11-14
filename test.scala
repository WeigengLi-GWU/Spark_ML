class ExceptionExample{  
    def divide(a:Int, b:Int) = {  
        try{  
            num/0
        }catch{  
            case e: ArithmeticException => println(e)  
        } 
    }  
}
object Demo{
    def main(args:Array[String]) : Unit={  
        var e = new ExceptionExample()
        e.divide(100,0)
        println("Test")
    }  
}

