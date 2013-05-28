package models

import scala.beans.BeanProperty

abstract class ReceivedMsg {
  
  var toUserName: String = _
  var fromUserName: String = _
  var createTime: Long = _
  var msgType: String = _
  var msgId: Int = _

}

object ReceivedMsg {
  
  def fromXml[T >: ReceivedMsg](xml: scala.xml.Node): T = {
    (xml \\ "MsgType" headOption).map(_.text).get match {
      case "text" => {
        val msg: T = new ReceivedTextMsg()
        msg
      }
      case _ => throw new Exception("unsupported messageType ")
    }
  }
  
}

class ReceivedTextMsg extends ReceivedMsg {
  
  var content: String = _

}

class ReceivedImageMsg extends ReceivedMsg {
  
  var picUrl: String = _
}

class ReceivedLocationMsg extends ReceivedMsg {
  
  var locationX: Double = _
  var locationY: Double = _
  var scale: Int = _
  var label: String = _
  
}

class ReceivedLinkMsg extends ReceivedMsg {
  
  var title: String = _
  var description: String = _
  var url: String = _
  
}

class ReceivedEventMsg extends ReceivedMsg {
  
  var event: String = _
  var eventKey: String = _
  
}

