import akka.actor.Actor
import akka.event.Logging
import akka.actor.ActorSystem
import akka.actor.Props
/**
 * Become 要求一个 PartialFunction[Any, Unit] 参数作为新的消息处理实现。 被替换的代码被存在一个栈中，可以被push和pop。
 * 请注意actor被其监管者重启后将恢复其最初的行为
 * 一个特别好的例子是用它来实现一个有限状态机 (FSM): Dining Hakkers.
 * 以下是另外一个使用 become and unbecome 的非常酷的小例子
 */
object ActorBecomeTest extends App {
  
  case object Swap
  
  class Swapper extends Actor {
    
    val log = Logging(context.system, this)
    
    def receive = {
      case Swap =>
        log.info("Hi")
        context.become {
          case Swap =>
            log.info("Ho")
            context.unbecome
        }
    }
  }
  
  val system = ActorSystem("swapperSystem")
  val swap = system.actorOf(Props[Swapper], name = "swapper")
  swap ! Swap
  swap ! Swap
  swap ! Swap
  
}
