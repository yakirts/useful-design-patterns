package org.example.structural




interface RowingBoat {
    fun row()
}

class FishingBoat {
    fun sail() {
        println("The fishing boat is sailing")
    }
}


class Captain(private val rowingBoat: RowingBoat) {
    fun row() {
        rowingBoat.row()
    }
}

class FishingBoatAdapter : RowingBoat {
    private val boat: FishingBoat = FishingBoat()
    override fun row() {
        boat.sail()
    }
}

fun main(){
    val captain = Captain(FishingBoatAdapter());
    captain.row()
}
