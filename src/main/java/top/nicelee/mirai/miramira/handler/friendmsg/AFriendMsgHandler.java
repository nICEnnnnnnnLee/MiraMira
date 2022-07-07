package top.nicelee.mirai.miramira.handler.friendmsg;

import java.lang.annotation.*;
/**
 * 标记该类成为以一个消息事件处理者
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AFriendMsgHandler {

	int priority() default 1;
}
