package top.nicelee.mirai.miramira.handler.normalmsg;

import java.lang.annotation.*;
/**
 * 标记该类成为以一个消息事件处理者
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ANormalMsgHandler {

	int priority() default 1;
}
