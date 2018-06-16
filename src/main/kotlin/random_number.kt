package project

import jason.asSemantics.*
import jason.asSyntax.*

import java.util.Random

@Suppress("ClassName")
class random_number : DefaultInternalAction() {

    override fun execute(ts: TransitionSystem,
                         un: Unifier,
                         args: Array<Term>): Any {
        return if (args[0].isNumeric && args[1].isNumeric) {
            val max = (args[0] as NumberTermImpl).solve().toInt()
            val min = (args[1] as NumberTermImpl).solve().toInt()
            un.unifies(args[2], NumberTermImpl((Random().nextInt(max) + min).toDouble()))
        }
        else false
    }
}