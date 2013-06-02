package models.handler

import models.ReceivedMsg
import models.ReplyMsg
import models.ReplyTextMsg

trait MsgHandler[A <: ReceivedMsg] {

  def hand(msg: A): ReplyMsg

  /**
   * 未知指令回复
   */
  val UNKNOW: ReplyTextMsg = {
    val reply = new ReplyTextMsg
    reply.setContent("表示不懂")
    reply
  }

}

trait MsgSupport {

  def supported(directive: String): Boolean

}