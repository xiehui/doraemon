import akka.actor.Actor
import akka.event.Logging
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.actors.Future

object ActorTest extends App {
  
  class MyActor extends Actor {
    val log = Logging(context.system, this)
    
    def receive = {
      case "test" => log.info("received test")
      case _ => log.info("received unknown message")
    }
  }
  
  val system = ActorSystem("MySystem")
  val myActor = system.actorOf(Props[MyActor], name = "myactor")
  myActor ! ""

}

