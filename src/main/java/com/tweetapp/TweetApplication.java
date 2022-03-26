package com.tweetapp;

import com.tweetapp.bean.ConsoleApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author Lakshit Yadav
 *
 */

@SpringBootApplication
public class TweetApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(TweetApplication.class, args);

		ConsoleApp consoleApp = applicationContext.getBean(ConsoleApp.class);
		consoleApp.runConsoleApplication();
	}

}
