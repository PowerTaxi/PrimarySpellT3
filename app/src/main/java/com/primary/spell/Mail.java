package com.primary.spell;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends javax.mail.Authenticator
{
    private String user;
    private String password;
    private String[] to;
    private String from;
    private String port;
    private String _sport;
    private String host;
    private String subject;
    private String body;
    private boolean auth;
    private boolean _debuggable;
    private Multipart _multipart;

    public Mail() 
	{
        host = "smtp.gmail.com"; // default smtp server
        port = "465"; // default smtp port
        _sport = "465"; // default socketfactory port

        user = ""; // username
        password = ""; // password
        from = ""; // email sent from
        subject = ""; // email subject
        body = ""; // email body

        _debuggable = false; // debug mode on or off - default off
        auth = true; // smtp authentication - default on

        _multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Mail(String users, String pass)
	{
        this();
        user = users;
        password = pass;
    }

    public boolean send() throws Exception 
	{
        Properties props = _setProperties();

        if(!user.equals("") && ! password.equals("") && to.length > 0 && ! from.equals("") && ! subject.equals("") && ! body.equals(""))
		{
            Session session = Session.getInstance(props, this);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] addressTo = new InternetAddress[to.length];
			
            for (int i = 0; i < to.length; i++)
			{
                addressTo[i] = new InternetAddress(to[i]);
            }
			
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);
            // Put parts in message
            msg.setContent(_multipart);
            // send email
            Transport.send(msg);
            return true;
        }
		return false;
    }

    public void addAttachment(String filename) throws Exception 
	{
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        _multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() 
	{
        return new PasswordAuthentication(user, password);
    }

    private Properties _setProperties() 
	{
        Properties props = new Properties();

        props.put("mail.smtp.host", host);

        if(_debuggable) 
		{
            props.put("mail.debug", "true");
        }

        if(auth)
		{
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", _sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }

    public String getBody() 
	{
        return body;
    }

    public void setBody(String body)
	{
        this.body = body;
    }

    public void setTo(String[] toArr) 
	{
        this.to=toArr;
    }

    public void setFrom(String string) 
	{
        this.from=string;
    }

    public void setSubject(String string) 
	{
        this.subject=string;
    }
}