package controllers

import play.api._
import play.api.mvc._

object ReceivedMsgController extends Controller {

  /**
   * <xml>
 <ToUserName><![CDATA[toUser]]></ToUserName>
 <FromUserName><![CDATA[fromUser]]></FromUserName> 
 <CreateTime>1348831860</CreateTime>
 <MsgType><![CDATA[text]]></MsgType>
 <Content><![CDATA[this is a test]]></Content>
 <MsgId>1234567890123456</MsgId>
 </xml>
   */
  def reply = Action(parse.xml) {
    request => (request.body \\ "MsgType" headOption).map(_.text).get match {
      case "text" => Ok("text")
    }
  }
  
}