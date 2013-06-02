package models.handler.text

import scala.beans.BeanProperty
import models.ReplyMsg
import models.ReceivedTextMsg

/**
 * 字符指令处理器
 */
class LetterDirectiveHandler extends DirectiveHandler {
  
  import scala.collection._

  val handlers = mutable.Map.empty[String, DirectiveHandler]
  
  def addHandler(directive : String, h : DirectiveHandler) {
    if(handlers.contains(directive)) {
      throw new RuntimeException("directive [" + directive + "] has more than on handler")
    }
    handlers(directive) = h
  }
  
  override def hand(directive: String, param: String, msg: ReceivedTextMsg) : ReplyMsg = {
    handlers.get(directive) match {
      case Some(handler) => handler.hand(directive, param, msg)
      case None => UNKNOW
    }
  }
  
}

object LetterDirectiveHandler {
  
  def apply(path: String): LetterDirectiveHandler = {
    var handler = new LetterDirectiveHandler;
    
    handler
  }
  
}