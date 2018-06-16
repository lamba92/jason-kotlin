package project

import jason.environment.grid.GridWorldView
import jason.environment.grid.Location

import java.awt.*

/**
 * class that implements the View of Domestic Robot application
 */
class HouseView(var hmodel: HouseModel) : GridWorldView(hmodel, "Domestic Robot", 700) {

    init {
        defaultFont = Font("Arial", Font.BOLD, 16) // change default font
        isVisible = true
        repaint()
    }

    /**
     * draw application objects
     */
    override fun draw(g: Graphics?, x: Int, y: Int, `object`: Int) {
        val lRobot = hmodel.getAgPos(0)
        super.drawAgent(g!!, x, y, Color.lightGray, -1)
        when (`object`) {
            HouseModel.FRIDGE -> {
                if (lRobot == hmodel.lFridge) {
                    super.drawAgent(g, x, y, Color.yellow, -1)
                }
                g.color = Color.black
                drawString(g, x, y, defaultFont, "Fridge (" + hmodel.availableBeers + ")")
            }
            HouseModel.OWNER -> {
                if (lRobot == hmodel.lOwner) {
                    super.drawAgent(g, x, y, Color.yellow, -1)
                }
                var o = "Owner"
                if (hmodel.sipCount > 0) {
                    o += " (" + hmodel.sipCount + ")"
                }
                g.color = Color.black
                drawString(g, x, y, defaultFont, o)
            }
        }
    }

    override fun drawAgent(g: Graphics, x: Int, y: Int, c: Color, id: Int) {
        var color = c
        val lRobot = hmodel.getAgPos(0)
        if (lRobot != hmodel.lOwner && lRobot != hmodel.lFridge) {
            color = Color.yellow
            if (hmodel.carryingBeer) color = Color.orange
            super.drawAgent(g, x, y, color, -1)
            g.color = Color.black
            super.drawString(g, x, y, defaultFont, "Robot")
        }
    }
}

