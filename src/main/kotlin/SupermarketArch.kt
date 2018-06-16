package project

import jason.architecture.AgArch
import jason.asSemantics.Message

class SupermarketArch : AgArch() {

    override fun checkMail() {

        // calls the default implementation to move all
        // messages to the circumstance's mailbox.
        super.checkMail()

        // gets an iterator for the circumstance's mailbox
        // and removes messages from owner
        val im = ts.c.mailBox.iterator()
        while (im.hasNext()) {
            val m = im.next() as Message
            if (m.sender == "owner") {
                im.remove()

                // sends a message to owner to inform that
                // his/her message was ignored
                val r = Message(
                        "tell",
                        agName,
                        m.sender,
                        "msg(\"You are not allowed to ask me for anything, only your robot can do that!\")")
                try {
                    sendMsg(r)
                } catch (e: Exception) {
                }

            }
        }
    }

}

