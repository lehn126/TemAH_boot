package com.temah.ahfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

// 在springboot中的里面存在spi机制，在AopAutoConfiguration这个类中默认会加上下面这个注解，
// 所以一般情况下是不需要手动再次加@EnableAspectJAutoProxy(proxyTargetClass = true)。
// 这个机制包含在spring-boot-autoconfigure包中，所以当项目中引入了这个包时就不需要自己引入EnableAspectJAutoProxy了。
// @EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(scanBasePackages = "com.temah.*")
public class TemAhAhfmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemAhAhfmApplication.class, args);
	}

}
