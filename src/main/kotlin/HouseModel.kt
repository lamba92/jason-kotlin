package project

import jason.environment.grid.GridWorldModel
import jason.environment.grid.Location

/** class that implements the Model of Domestic Robot application  */
// create a 7x7 grid with one mobile agent
class HouseModel : GridWorldModel(GSize, GSize, 1) {

    companion object {

        // constants for the grid objects
        const val FRIDGE = 16
        const val OWNER = 32

        // the grid size
        const val GSize = 7
    }

    var fridgeOpen = false // whether the fridge is open
    var carryingBeer = false // whether the robot is carrying beer
    var sipCount = 0 // how many sip the owner did

    var availableBeers = 2 // how many beers are available
    var lFridge = Location(0, 0)

    var lOwner = Location(GSize - 1, GSize - 1)

    internal val beer: Boolean
        get() {
            return if (fridgeOpen && availableBeers > 0 && !carryingBeer) {
                availableBeers--
                carryingBeer = true
                view.update(lFridge.x, lFridge.y)
                true
            } else false

        }

    init {
        // initial location of robot (column 3, line 3)
        // ag code 0 means the robot
        setAgPos(0, GSize / 2, GSize / 2)

        // initial location of fridge and owner
        add(FRIDGE, lFridge)
        add(OWNER, lOwner)
    }

    internal fun openFridge(): Boolean {
        return if (!fridgeOpen) {
            fridgeOpen = true
            true
        } else false
    }

    internal fun closeFridge(): Boolean {
        return if (fridgeOpen) {
            fridgeOpen = false
            true
        } else false
    }

    internal fun moveTowards(dest: Location): Boolean {
        val r1 = getAgPos(0)
        if (r1.x < dest.x)
            r1.x++
        else if (r1.x > dest.x) r1.x--
        if (r1.y < dest.y)
            r1.y++
        else if (r1.y > dest.y) r1.y--
        setAgPos(0, r1) // move the robot in the grid

        // repaint the fridge and owner locations
        view.update(lFridge.x, lFridge.y)
        view.update(lOwner.x, lOwner.y)
        return true
    }

    internal fun addBeer(n: Int): Boolean {
        availableBeers += n
        view.update(lFridge.x, lFridge.y)
        return true
    }

    internal fun handInBeer(): Boolean {
        return if (carryingBeer) {
            sipCount = 10
            carryingBeer = false
            view.update(lOwner.x, lOwner.y)
            true
        } else false
    }

    internal fun sipBeer(): Boolean {
        return if (sipCount > 0) {
            sipCount--
            view.update(lOwner.x, lOwner.y)
            true
        } else false
    }
}

