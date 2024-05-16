package com.inn.cafe.management.utils;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailUtils {

	private final JavaMailSender mailSender;

	public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("rustem.huseynov.2015@bk.ru");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if (list != null && list.size() > 0)
			message.setCc(getCcArray(list));
		mailSender.send(message);

	}

	private String[] getCcArray(List<String> ccList) {
		String[] cc = new String[ccList.size()];
		for (int i = 0; i < ccList.size(); i++) {
			cc[i] = ccList.get(i);
		}
		return cc;
	}
}
