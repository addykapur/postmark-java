package com.wildbit.java.postmark.client.data.model.message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import eu.medsea.mimeutil.MimeUtil;

/**
 * Base email message object
 */
public class BaseMessage {
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String replyTo;
    private String subject;
    private String htmlBody;
    private String textBody;
    private String tag;
    private ArrayList<Header> headers;
    private ArrayList<HashMap<String, String>> attachments;

    public BaseMessage() {
        this.attachments = new ArrayList<>();
    }

    public BaseMessage(String from, String to, String subject, String htmlBody) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.htmlBody = htmlBody;
        this.attachments = new ArrayList<>();
    }

    public BaseMessage(String from, String to, String subject, String htmlBody, String textBody) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.htmlBody = htmlBody;
        this.textBody = textBody;
        this.attachments = new ArrayList<>();
    }

    // SETTERS AND GETTERS

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Helper method for setting recipients with email address and their Full Name.
     *
     * @param to recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setTo(HashMap<String,String> to) {
        this.to = convertRecipients(to);
    }

    /**
     * Helper method for setting Array of recipient email addresses.
     *
     * @param to recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setTo(ArrayList<String > to) {
        this.to = convertRecipients(to);
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * Helper method for setting recipients with email address and their Full Name.
     *
     * @param cc recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setCc(HashMap<String,String> cc) {
        this.cc = convertRecipients(cc);
    }

    /**
     * Helper method for setting Array of recipient email addresses.
     *
     * @param cc recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setCc(ArrayList<String > cc) {
        this.cc = convertRecipients(cc);
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    /**
     * Helper method for setting recipients with email address and their Full Name.
     *
     * @param bcc recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setBcc(HashMap<String,String> bcc) {
        this.bcc = convertRecipients(bcc);
    }

    /**
     * Helper method for setting Array of recipient email addresses.
     *
     * @param bcc recipients list
     * @see #convertRecipients(HashMap) for details
     */
    public void setBcc(ArrayList<String > bcc) {
        this.bcc = convertRecipients(bcc);
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<Header> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        if (this.headers == null) {
            setHeaders(new ArrayList<>());
        }
        this.headers.add(new Header(name,value));
    }

    public void clearHeaders() {
        if (this.headers != null) {
            this.headers.clear();
        }
    }

    public ArrayList<HashMap<String, String>> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<HashMap<String, String>> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(String path) throws IOException {
        HashMap<String, String> attachment = new HashMap<>();
        attachment.put("Name", new File(path).getName());
        attachment.put("Content", readFileContent(path));
        attachment.put("ContentType", readFileContentType(path));

        addAttachment(attachment);
    }

    public void addAttachment(HashMap<String, String> attachment) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        attachments.add(attachment);
    }

    public void addAttachments(ArrayList<HashMap<String, String>> attachments) {
        attachments.forEach(this::addAttachment);
    }

    private String readFileContent(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String readFileContentType(String path) {
        File file = new File(path);
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        return MimeUtil.getMimeTypes(file).toString();
    }

    /**
     * This helper method allows setting list of recipients which will contain full name next to email address.
     * By passing something like HashMap of: key -> John Smith, value -> john@example.com
     * you would be able to send email to recipient John Smith ("John Smith" <john@example.com>)
     *
     * @param recipients recipient hashmap, with Full Name, Email address pairs.
     */
    private String convertRecipients(HashMap<String,String> recipients) {

        StringBuilder recipientsString = new StringBuilder();

        Iterator<HashMap.Entry<String, String>> entries = recipients.entrySet().iterator();
        while (entries.hasNext()) {
            HashMap.Entry<String, String> entry = entries.next();
            recipientsString.append("\"").append(entry.getKey()).append("\"").append("<").append(entry.getValue()).append(">");
            if (entries.hasNext()) { recipientsString.append(","); }
        }

        return recipientsString.toString();
    }

    private String convertRecipients(ArrayList<String> recipients) {

        StringBuilder recipientsString = new StringBuilder();

        Iterator<String> entries = recipients.iterator();
        while (entries.hasNext()) {
            String entry = entries.next();
            recipientsString.append("\"").append(entry).append("\"");
            if (entries.hasNext()) { recipientsString.append(","); }
        }

        return recipientsString.toString();
    }

}
