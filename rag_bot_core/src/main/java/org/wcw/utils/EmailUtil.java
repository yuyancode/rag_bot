package org.wcw.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.wcw.common.exception.EmailException;
import org.wcw.config.properties.EmailProperties;

import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮箱验证码
     * @param to 用户邮箱
     * @param code 验证码
     */
    public void sendCodeVerifyEmail(String to, String code) {
        String subject = "绑定邮箱验证码校验";
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("【RagBot】" + subject);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(code, true);
            mailSender.send(message);
            log.info("邮箱验证码发送成功: {}", code);
        } catch (MessagingException e) {
            log.info("邮箱验证码发送失败");
            throw new EmailException("邮箱验证码发送失败");
        }
    }

    /**
     * 发送用户反馈信息邮件给作者
     * @param subject
     * @param content
     */
    public void sendFeedbackEmail(String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(emailProperties.getToAddress());
            helper.setFrom(emailProperties.getFromAddress());
            content = String.format(
                    emailProperties.getTemplate(),
                    FORMATTER.format(java.time.LocalDateTime.now()),
                    content
            );
            helper.setText(content, true);
            mailSender.send(message);
            log.info("用户反馈邮件发送到作者邮箱");
        } catch (MessagingException e) {
            log.info("用户反馈邮件发送失败");
            throw new EmailException("用户反馈邮件发送失败");
        }
    }
}
