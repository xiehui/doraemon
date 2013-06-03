package models.handler.text

import models.ReplyMsg
import models.ReceivedTextMsg

class NumberDirectiveHandler extends DirectiveHandler {

  override def hand(directive: String, param: String, msg: ReceivedTextMsg) : ReplyMsg = {
    null
  }
}