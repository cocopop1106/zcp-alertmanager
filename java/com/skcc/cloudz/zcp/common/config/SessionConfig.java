package com.skcc.cloudz.zcp.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession
@PropertySource("classpath:application.properties")
public class SessionConfig
{
	@Value("${redis.host}")
	String host;
 
	@Value("${redis.port}")
	int port;
	
	@Value("${redis.database}")
	int database;
	
	@Bean
	public JedisConnectionFactory connectionFactory()
	{
		JedisConnectionFactory conn = new JedisConnectionFactory();
		conn.setHostName(host);
		conn.setPort(port);
		conn.setDatabase(database);
		conn.setUsePool(true);
		System.out.println(conn);
		return conn;
	}
 
	@Bean
	public CookieSerializer cookieSerializer()
	{
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		// 위 레디스 처럼 serializer 의 각종 설정 가능.
		// tomcat context 로 설정한 쿠키 기능들도 여기서 설정가능.
		return serializer;
	}
}
