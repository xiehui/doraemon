package models

import scala.beans.BeanProperty

abstract class ReceivedMsg {

  @BeanProperty
  var toUserName: String = _
  @BeanProperty
  var fromUserName: String = _
  @BeanProperty
  var createTime: Long = _
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
      setCreateTime(content.text.toLong)
    case <MsgType>{ content }</MsgType> =>
      setMsgType(content.text)
    case <MsgId>{ content }</MsgId> =>
      setMsgId(content.text.toLong)
  }

}

class ReceivedTextMsg extends ReceivedMsg {

  @BeanProperty
  var content: String = _

  override def parse = {
    super.parse.orElse({
      case <Content>{ content }</Content> =>
        setContent(content.text)
    })
  }

}

class ReceivedImageMsg extends ReceivedMsg {

  @BeanProperty
  var picUrl: String = _

  override def parse = {
    super.parse.orElse({
      case <PicUrl>{ content }</PicUrl> =>
        setPicUrl(content.text)
    })
  }
}

class ReceivedLocationMsg extends ReceivedMsg {

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

class ReceivedLinkMsg extends ReceivedMsg {

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

class ReceivedEventMsg extends ReceivedMsg {

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
        case "text" => new ReceivedTextMsg
        case "image" => new ReceivedImageMsg
        case "location" => new ReceivedLocationMsg
        case "link" => new ReceivedLinkMsg
        case "event" => new ReceivedEventMsg
      }
      xml.child.filter(_.isInstanceOf[scala.xml.Elem]).foreach(n => msg.parse(n))
      msg
  }

}