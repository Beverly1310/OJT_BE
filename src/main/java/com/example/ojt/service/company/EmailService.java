package com.example.ojt.service.company;

import com.example.ojt.model.dto.response.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    // Phương thức gửi email đơn giản với thông tin từ MailBody
    public void sendSimpleMessage(MailBody mailBody){
        // Tạo đối tượng SimpleMailMessage đại diện cho email
        SimpleMailMessage message = new SimpleMailMessage();
        // Thiết lập địa chỉ người nhận từ mailBody
        message.setTo(mailBody.to());
        // Thiết lập địa chỉ người gửi (có thể thay đổi nếu cần)
//        message.setFrom("giangpc7@gmail.com");
        // Thiết lập tiêu đề email từ mailBody
        message.setSubject(mailBody.subject());
        // Thiết lập nội dung email từ mailBody
        message.setText(mailBody.text());

        // Gửi email bằng JavaMailSender
        javaMailSender.send(message);
    }
}