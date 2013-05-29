package models

import scala.beans.BeanProperty

abstract class ReplyMsg {

  @BeanProperty
  var toUserName: String = _
  
}