package models

import scala.beans.BeanProperty

import org.joda.time.DateTime

abstract class ReplyMsg {

  @BeanProperty
  var toUserName: String = _
  @BeanProperty
  var fromUserName: String = _
  @BeanProperty
  var createTime: DateTime = _
  def msgType: String
  @BeanProperty
  var funcFlag: Int = _

  def toXml: scala.xml.Node
  
  def flush(received: ReceivedMsg) {
    toUserName = received.getFromUserName
    fromUserName = received.getToUserName
    createTime = DateTime.now
  }

}

class ReplyTextMsg extends ReplyMsg {

  override def msgType = "text"
    
  @BeanProperty
  var content: String = _
  
  override def toXml =
    <xml>
      <ToUserName>{ toUserName }</ToUserName>
      <FromUserName>{ fromUserName }</FromUserName>
      <CreateTime>{ createTime }</CreateTime>
      <MsgType>{ msgType }</MsgType>
      <Content>{ content }</Content>
      <FuncFlag>{ funcFlag }</FuncFlag>
    </xml>

}

class ReplyMusicMsg extends ReplyMsg {

  override def msgType = "music"
    
  @BeanProperty
  var music: Music = _

  override def toXml =
    <xml>
      <ToUserName>{ toUserName }</ToUserName>
      <FromUserName>{ fromUserName }</FromUserName>
      <CreateTime>{ createTime }</CreateTime>
      <MsgType>{ msgType }</MsgType>
      <Music>
        <Title>{ music.title }</Title>
        <Description>{ music.description }</Description>
        <MusicUrl>{ music.musicUrl }</MusicUrl>
        <HQMusicUrl>{ music.HQMusicUrl }</HQMusicUrl>
      </Music>
      <FuncFlag>{ funcFlag }</FuncFlag>
    </xml>
}

class Music {

  @BeanProperty
  var title: String = _
  @BeanProperty
  var description: String = _
  /**
   * 音乐链接
   */
  @BeanProperty
  var musicUrl: String = _
  /**
   * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
   */
  @BeanProperty
  var HQMusicUrl: String = _
}

/**
 * 回复图文消息
 */
class ReplyNewsMsg extends ReplyMsg {

  override def msgType = "news"
    
  @BeanProperty
  var articles: List[Article] = _

  override def toXml =
    <xml>
      <ToUserName>{ toUserName }</ToUserName>
      <FromUserName>{ fromUserName }</FromUserName>
      <CreateTime>{ createTime }</CreateTime>
      <MsgType>{ msgType }</MsgType>
      <ArticleCount>{ articles.size }</ArticleCount>
      <Articles>
        {
          articles.map(item =>
            <item>
              <Title>{ item.title }</Title>
              <Description>{ item.description }</Description>
              <PicUrl>{ item.picUrl }</PicUrl>
              <Url>{ item.url }</Url>
            </item>)
        }
      </Articles>
      <FuncFlag>{ funcFlag }</FuncFlag>
    </xml>
}

class Article {

  @BeanProperty
  var title: String = _
  @BeanProperty
  var description: String = _
  @BeanProperty
  var picUrl: String = _
  @BeanProperty
  var url: String = _

}

object ReplyMsgTest extends App {

  val newsMsg: ReplyNewsMsg = new ReplyNewsMsg
  newsMsg.setCreateTime(DateTime.now())
  newsMsg.setFromUserName("from")
  newsMsg.setToUserName("to")

  val article: Article = new Article;
  article.setTitle("title")
  article.setDescription("xxxxxxxxxx")
  article.setPicUrl("image:\\xxxxx")
  article.setUrl("http://gezishu.com")

  newsMsg.setArticles(List(article))

  print(newsMsg.toXml)

}