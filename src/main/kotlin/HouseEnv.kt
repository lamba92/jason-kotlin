package project

import jason.NoValueException
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure
import jason.environment.Environment

class HouseEnv : Environment() {

    companion object {
        // common literals
        val of = Literal.parseLiteral("open(fridge)")!!
        val clf = Literal.parseLiteral("close(fridge)")!!
        val gb = Literal.parseLiteral("get(beer)")!!
        val hb = Literal.parseLiteral("hand_in(beer)")!!
        val sb = Literal.parseLiteral("sip(beer)")!!
        val hob = Literal.parseLiteral("has(owner,beer)")!!

        val af = Literal.parseLiteral("at(robot,fridge)")!!
        val ao = Literal.parseLiteral("at(robot,owner)")!!
    }

    lateinit var model: HouseModel // the model of the grid

    override fun init(args: Array<String>?) {
        model = HouseModel()

        if (args!!.size == 1 && args[0] == "gui") {
            val view = HouseView(model)
            model.setView(view)
        }

        updatePercepts()
    }

    /**
     * creates the agents percepts based on the HouseModel
     */
    fun updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("robot")
        clearPercepts("owner")

        // get the robot location
        val lRobot = model.getAgPos(0)

        // add agent location to its percepts
        if (lRobot == model.lFridge) {
            addPercept("robot", af)
        }
        if (lRobot == model.lOwner) {
            addPercept("robot", ao)
        }

        // add beer "status" to the percepts
        if (model.fridgeOpen) {
            addPercept("robot", Literal.parseLiteral("stock(beer," + model.availableBeers + ")"))
        }
        if (model.sipCount > 0) {
            addPercept("robot", hob)
            addPercept("owner", hob)
        }
    }

    override fun executeAction(ag: String?, action: Structure): Boolean {
        println("[$ag] doing: $action")
        val result = when {
            action == of -> model.openFridge() // of = open(fridge)
            action == clf -> model.closeFridge() // clf = close(fridge)
            action == gb -> model.beer
            action == hb -> model.handInBeer()
            action == sb -> model.sipBeer()
            action.functor == "move_towards" -> {
                val l = action.getTerm(0).toString()
                when (l){
                    "fridge" -> model.moveTowards(model.lFridge)
                    "owner" -> model.moveTowards(model.lOwner)
                    else -> false
                }
            }
            action.functor == "deliver" -> {
                // wait 4 seconds to finish "deliver"
                try {
                    Thread.sleep(4000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    model.addBeer((action.getTerm(1) as NumberTerm).solve().toInt())
                } catch (e: NoValueException) {
                    e.printStackTrace()
                }
                return true
            }
            else -> {
                System.err.println("Failed to execute action $action")
                return true
            }
        }

        if (result) {
            updatePercepts()
            try {
                Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }
}

