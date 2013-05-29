package models

object ReceivedMsgTest extends App {
  
  val xml = 
    <xml>
		  <ToUserName><![CDATA[toUser]]></ToUserName>
		  <FromUserName><![CDATA[fromUser]]></FromUserName> 
		  <CreateTime>1348831860</CreateTime>
		  <MsgType><![CDATA[text]]></MsgType>
		  <Content><![CDATA[this is a test]]></Content>
		  <MsgId>1234567890123456</MsgId>
    </xml>;
  xml.child.filter(_.isInstanceOf[scala.xml.Elem]).foreach(x => println(x+"="))
  val msg = ReceivedMsgDispatcher.fromXml(xml);
  println(msg)
  

}