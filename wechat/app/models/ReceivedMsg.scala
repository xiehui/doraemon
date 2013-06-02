package models

import scala.beans.BeanProperty
import org.joda.time.DateTime

abstract class ReceivedMsg {

  @BeanProperty
  var toUserName: String = _
  @BeanProperty
  var fromUserName: String = _
  @BeanProperty
  var createTime: DateTime = _
  @BeanProperty
  var msgType: String = _
  @BeanProperty
  var msgId: Long = _

  def parse: PartialFunction[scala.xml.Node, Unit] = {
    case <ToUserName>{ content }</ToUserName> =>
      setToUserName(content.text)
    case <FromUserName>{ content }</FromUserName> =>
      setFromUserName(content.text)
    case <CreateTime>{ content }</CreateTime> =>
      setCreateTime(new DateTime(content.text.toLong))
    case <MsgType>{ content }</MsgType> =>
      setMsgType(content.text)
    case <MsgId>{ content }</MsgId> =>
      setMsgId(content.text.toLong)
  }

}

case class ReceivedTextMsg() extends ReceivedMsg {

  @BeanProperty
  var content: String = _

  override def parse = {
    super.parse.orElse({
      case <Content>{ content }</Content> =>
        setContent(content.text)
    })
  }

}

case class ReceivedImageMsg() extends ReceivedMsg {

  @BeanProperty
  var picUrl: String = _

  override def parse = {
    super.parse.orElse({
      case <PicUrl>{ content }</PicUrl> =>
        setPicUrl(content.text)
    })
  }
}

case class ReceivedLocationMsg() extends ReceivedMsg {

  @BeanProperty
  var locationX: Double = _
  @BeanProperty
  var locationY: Double = _
  @BeanProperty
  var scale: Int = _
  @BeanProperty
  var label: String = _

  override def parse = {
    super.parse.orElse({
      case <LocationX>{ content }</LocationX> =>
        setLocationX(content.text.toDouble)
      case <LocationY>{ content }</LocationY> =>
        setLocationY(content.text.toDouble)
      case <Scale>{ content }</Scale> =>
        setScale(content.text.toInt)
      case <Label>{ content }</Label> =>
        setLabel(content.text)
    })
  }

}

case class ReceivedLinkMsg() extends ReceivedMsg {

  @BeanProperty
  var title: String = _
  @BeanProperty
  var description: String = _
  @BeanProperty
  var url: String = _

  override def parse = {
    super.parse.orElse({
      case <Title>{ content }</Title> =>
        setTitle(content.text)
      case <Description>{ content }</Description> =>
        setDescription(content.text)
      case <Url>{ content }</Url> =>
        setUrl(content.text)
    })
  }

}

case class ReceivedEventMsg() extends ReceivedMsg {

  @BeanProperty
  var event: String = _
  @BeanProperty
  var eventKey: String = _

  override def parse = {
    super.parse.orElse({
      case <Event>{ content }</Event> =>
        setEvent(content.text)
      case <EventKey>{ content }</EventKey> =>
        setEventKey(content.text)
    })
  }

}

object ReceivedMsgDispatcher {

  def fromXml[T >: ReceivedMsg](xml: scala.xml.Node): T = {
      val msg = (xml \\ "MsgType" headOption).map(_.text).get match {
        case "text" => ReceivedTextMsg()
        case "image" => ReceivedImageMsg()
        case "location" => ReceivedLocationMsg()
        case "link" => ReceivedLinkMsg()
        case "event" => ReceivedEventMsg()
      }
      xml.child.filter(_.isInstanceOf[scala.xml.Elem]).foreach(n => msg.parse(n))
      msg
  }

}