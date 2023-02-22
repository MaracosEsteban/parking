

class Parking() {
    private data class Spot(val number:Int, var color: String = "", var occupied: Boolean = false) {
       // val number = nextNumber();// todo    estoy usando una variable global porque no se como se manejan las estáticas en kotlin
        var regisNum:String=""
    }

    private val parkingSpots = mutableListOf<Spot>()
    var Continue = true
    private  var contador:Int=0
    private var created=false

    fun readCommand(command: String): String {
        val tokens: List<String> = command.split(" ")
        when (tokens[0]) {
            "create"-> return(create(tokens[1].toInt()))
            "park"  -> return (if( created){park(tokens[1],tokens[2])}else {"Sorry, a parking lot has not been created."})
            "leave" -> return(if( created){leave(tokens[1].toInt())}else {"Sorry, a parking lot has not been created."})
            "status" -> return(if( created){status()}else {"Sorry, a parking lot has not been created."})
            //reg_by_color
            "reg_by_color"  -> return(if( created){reg_by_color(tokens[1])}else {"Sorry, a parking lot has not been created."})
            "spot_by_color" -> return(if( created){spot_by_color(tokens[1])}else {"Sorry, a parking lot has not been created."})
            "spot_by_reg" -> return(if( created){spot_by_reg(tokens[1])}else {"Sorry, a parking lot has not been created."})
            "Exit", "exit" -> {
                this.Continue = false
                return ("Bye")
            }
            else -> return ("comando no válido")
        }
    }


    private fun status(): String {
        var string = ""
        var cant = 0
        for (x in 0..parkingSpots.lastIndex) {
            if (parkingSpots[x].occupied == true) {
                if (cant > 0) {
                    string += "\n"
                }
                string += "${parkingSpots[x].number} ${parkingSpots[x].regisNum} ${parkingSpots[x].color}"
                cant++
            }
        }
        if (cant == 0) {
            return "Parking lot is empty."
        }
        else {
            return string
        }
    }


    fun create(numLots: Int): String {
        contador=0
        parkingSpots.clear()
        repeat(numLots) {
            parkingSpots.add(Spot(++contador))
        }
        created = true
        return ("Created a parking lot with $numLots spots.")
    }


    fun park(plate: String, color: String): String {
        val carColor = color.substring(0,1).uppercase()+color.substring(1,color.length).lowercase()
        var libre = false
        var numSpot = 0
        for (x in 0..parkingSpots.lastIndex) {
            if (parkingSpots[x].occupied == false) {
                //obtengo el numero del parque libre y cambia bandera
                numSpot = parkingSpots[x].number
                libre = true
                // aparco el coche cambiendo los datos
                parkingSpots[x].occupied = true
                parkingSpots[x].regisNum = plate
                parkingSpots[x].color = carColor
                break
            }
        }
        if (libre) {
            return ("$carColor car parked in spot $numSpot.")
        }
        else {
            return ("Sorry, the parking lot is full.")
        }
    }



    private fun leave(number: Int): String {
        var found = false
        var indice = -1
        for (x in 0..parkingSpots.lastIndex) {
            if (parkingSpots[x].number == number) {
                found = true
                indice = x
                break
            }
        }
        if (found) {
            if (parkingSpots[indice].occupied == false) {
                return ("There is no car in spot $number.")
            }
            else {
                parkingSpots[indice].occupied = false
                return ("Spot $number is free.")
            }
        }
        else {
            return ("No existe ese garage")
        }
    }


    private fun reg_by_color(color: String):String {
        val carColor = color.substring(0,1).uppercase()+color.substring(1,color.length).lowercase()
        val filtered =parkingSpots.filter { it.color==carColor && it.occupied}
        if (filtered.size==0) {
            return "No cars with color $color were found."
        }
        else {
            var chain = ""
            for(x in 0..filtered.lastIndex) {
                chain += filtered[x].regisNum
                if (x != filtered.lastIndex) {
                    chain += ", "
                }
            }
            return chain
        }
    }


    private fun spot_by_color(color: String):String {
        val carColor = color.substring(0,1).uppercase()+color.substring(1,color.length).lowercase()
        val filtered =parkingSpots.filter { it.color==carColor && it.occupied}
        if (filtered.size==0) {
            return "No cars with color $color were found."
        }
        else {
            var chain = ""
            for(x in 0..filtered.lastIndex) {
                chain += filtered[x].number.toString()
                if (x != filtered.lastIndex) {
                    chain += ", "
                }
            }
            return chain
        }
    }

    private fun spot_by_reg(reg: String):String {
        val filtered =parkingSpots.filter { it.regisNum==reg && it.occupied }
        return if (filtered.size==0) {  "No cars with registration number $reg were found."} else { filtered[0].number.toString()}
    }

}


fun main() {
    val park1 = Parking() // Creo una instancia de la clase park
    while (park1.Continue) {
        val command = readln()
        val reply = park1.readCommand(command)
        if(park1.Continue) {
            println(reply)
        }
    }
}