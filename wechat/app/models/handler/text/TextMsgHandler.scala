package models.handler.text

import models.ReceivedTextMsg
import models.ReplyMsg
import models.handler.MsgHandler
import models.ReplyTextMsg

/**
 * 新议题信息处理
 */
class IssueMsgHandler extends MsgHandler[ReceivedTextMsg] {
  
  val letterHandler: LetterDirectiveHandler = LetterDirectiveHandler("")

  override def hand(msg: ReceivedTextMsg): ReplyMsg = {
    val index: Int = msg.content.indexOf(" ")
    var directive: String = ""
    var param: String = ""
    if (index > 0) {
      directive = msg.content.substring(0, index)
      param = msg.content.substring(index)
    } else {
      directive = msg.content
    }
    val letter = """([a-zA-Z]*)""".r
    val number = """([0-9]*)""".r
    val chinese = """([u4e00-u9fa5]*)""".r
    val reply: ReplyMsg = directive match {
      case letter(directive) =>
        letterHandler.hand(directive, param, msg)
        
    }
    reply flush msg
    reply
  }
}

class TalkMsgHandler extends MsgHandler[ReceivedTextMsg] {
  
  override def hand(msg: ReceivedTextMsg): ReplyMsg = {
    null
  }
}
/**
 * 用户回答处理
 */
trait AnswerMsgHandler {
  
  def isDefineAt(msg: ReceivedTextMsg) : Boolean = {
    hand.isDefinedAt(msg)
  }
  
  def hand:PartialFunction[ReceivedTextMsg, ReplyMsg]
  
}

/**
 * 指令处理
 */
trait DirectiveHandler extends MsgHandler[ReceivedTextMsg] {
  
  override def hand(msg: ReceivedTextMsg): ReplyMsg = 
    throw new UnsupportedOperationException("please use function hand(directive: String, param: String)")
  
  def hand(directive: String, param: String, msg: ReceivedTextMsg): ReplyMsg
  
}