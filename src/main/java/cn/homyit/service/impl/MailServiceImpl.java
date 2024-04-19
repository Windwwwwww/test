package cn.homyit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

@Service
//@Data
public class MailServiceImpl {
    @Autowired
    private JavaMailSender  javaMailSender;
    //     获取yml配置的发送者邮箱
    @Value("${spring.mail.username}")
    private String mainUserName;

    //发送人昵称
    @Value("智慧教育云平台官方团队")
    private String nickname;


    public boolean sendSimpleMail(String to,String name) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject("欢迎加入智慧教育云平台！");
        // 设置邮件发送者，昵称+<邮箱地址>
        message.setFrom(nickname + '<' + mainUserName + '>');
        // 设置邮件接收者，可以有多个接收者，多个接受者参数需要数组形式
        if (!EmailValidator.isValidEmail(to)){
            return false;
        }
        message.setTo(to);
        // 设置邮件抄送人，可以有多个抄送人
        message.setCc("2637432237@qq.com");
        // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(name + "你好！恭喜你成功注册融合深度学习与知识图谱的智慧教育云平台。\n希望您使用本平台愉快！");
        message.setText(
                "尊敬的用户," + name + ":\n\n" +
                        "您好！感谢您注册成为我们融合深度学习与知识图谱的智慧教育云平台的一员。我们诚挚地欢迎您的加入！\n\n" +
                        "通过我们的平台，您将能够获得最新的教育技术和资源，提升学习效率，拓展知识视野。我们致力于为您提供优质的教育体验，帮助您在学习和成长的道路上取得更大的成功。\n\n" +
                        "如果您有任何问题或需要帮助，请随时联系我们的客服团队。我们将竭诚为您提供支持和帮助。\n\n" +
                        "再次感谢您选择我们的平台，期待与您共同探索智慧教育的未来！\n\n" +
                        "祝您学习愉快！\n\n" +
                        "智慧教育云平台团队");
        // 发送邮件
        try {
            javaMailSender.send(message);
            return true;
        }catch (MailSendException me){
            return false;
        }
    }

}
//验证邮箱格式
class EmailValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}