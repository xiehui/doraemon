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
  var msgId: Int = _

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
      setMsgId(content.text.toInt)
  }

}

trait ReceivedMsgFromXml {

  def fromXml: PartialFunction[scala.xml.Node, ReceivedMsg]

}

object ReceivedMsg {

  def fromXml[T >: ReceivedMsg](xml: scala.xml.Node): T = {
    val fun = ReceivedTextMsg.fromXml.orElse(ReceivedImageMsg.fromXml).orElse(ReceivedLocationMsg.fromXml)
    		.orElse(ReceivedLinkMsg.fromXml).orElse(ReceivedEventMsg.fromXml)
    fun(xml)
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

object ReceivedTextMsg extends ReceivedMsgFromXml {

  override def fromXml: PartialFunction[scala.xml.Node, ReceivedTextMsg] = {
    xml =>
      (xml \\ "MsgType" headOption).map(_.text).get match {
        case "text" =>
          val msg: ReceivedTextMsg = new ReceivedTextMsg
          xml.foreach(n => msg.parse(n))
          msg
      }
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

object ReceivedImageMsg extends ReceivedMsgFromXml {

  override def fromXml: PartialFunction[scala.xml.Node, ReceivedImageMsg] = {
    xml =>
      (xml \\ "MsgType" headOption).map(_.text).get match {
        case "image" =>
          val msg: ReceivedImageMsg = new ReceivedImageMsg
          xml.foreach(n => msg.parse(n))
          msg
      }
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

object ReceivedLocationMsg extends ReceivedMsgFromXml {

  override def fromXml: PartialFunction[scala.xml.Node, ReceivedLocationMsg] = {
    xml =>
      (xml \\ "MsgType" headOption).map(_.text).get match {
        case "location" =>
          val msg: ReceivedLocationMsg = new ReceivedLocationMsg
          xml.foreach(msg.parse)
          msg
      }
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

object ReceivedLinkMsg extends ReceivedMsgFromXml {

  override def fromXml: PartialFunction[scala.xml.Node, ReceivedLinkMsg] = {
    xml =>
      (xml \\ "MsgType" headOption).map(_.text).get match {
        case "link" =>
          val msg: ReceivedLinkMsg = new ReceivedLinkMsg
          xml.foreach(msg.parse)
          msg
      }
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

object ReceivedEventMsg extends ReceivedMsgFromXml {

  override def fromXml: PartialFunction[scala.xml.Node, ReceivedEventMsg] = {
    xml =>
      (xml \\ "MsgType" headOption).map(_.text).get match {
        case "event" =>
          val msg: ReceivedEventMsg = new ReceivedEventMsg
          xml.foreach(msg.parse)
          msg
      }
  }

}


