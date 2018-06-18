package br.com.scopus.bot.messenger.banking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.scopus.messenger.core.Bot;
import br.com.scopus.messenger.core.common.Controller;
import br.com.scopus.messenger.core.common.EventType;
import br.com.scopus.messenger.core.common.IBot;
import br.com.scopus.messenger.core.models.Attachment;
import br.com.scopus.messenger.core.models.Button;
import br.com.scopus.messenger.core.models.Event;
import br.com.scopus.messenger.core.models.Message;
import br.com.scopus.messenger.core.models.Payload;

/**
 * 
 * @author dgarcia
 *
 */
@IBot
@Service
public class FbBot extends Bot {

    /**
     * Set this property in {@code application.properties}.
     */
    @Value("${fbBotToken}")
    private String fbToken;

    /**
     * Set this property in {@code application.properties}.
     */
    @Value("${fbPageAccessToken}")
    private String pageAccessToken;

    @Override
    public String getFbToken() {
        return fbToken;
    }

    @Override
    public String getPageAccessToken() {
        return pageAccessToken;
    }

    @Value("${text.messenger.extrato}")
    private String msgExtrato;

    /**
     * Sets the "Get Started" button with a payload "hi". It also set the "Greeting Text" which the
     * user sees when it opens up the chat window. Uncomment the {@code @PostConstruct} annotation
     * only after you have verified your webhook.
     */
    // @PostConstruct
    public void init() {
        setGetStartedButton("hi");
        setGreetingText(new Payload[] { new Payload().setLocale("default")
            .setText("JBot is a Java Framework to help"
                + " developers make Facebook, Slack and Twitter bots easily. You can see a quick demo by clicking "
                + "the \"Get Started\" button.") });
    }

    /**
     * This method gets invoked when a user clicks on the "Get Started" button or just when someone
     * simply types hi, hello or hey. When it is the former, the event type is
     * {@code EventType.POSTBACK} with the payload "hi" and when latter, the event type is
     * {@code EventType.MESSAGE}.
     *
     * @param event
     */
    @Controller(events = { EventType.MESSAGE }, pattern = "^(?i)(ola|olá|oi|como vai|ops)$")
    public void onGetStarted(Event event) {

        // quick reply buttons
        Button[] quickReplies = new Button[] {
            new Button().setContentType("text").setTitle("Saldo").setPayload("Saldo"),
            new Button().setContentType("text").setTitle("Extrato").setPayload("Extrato"),
            new Button().setContentType("text").setTitle("Recarga").setPayload("Recarga") };
        reply(event, new Message().setText("Em que posso ajudar?").setQuickReplies(quickReplies));
    }

    /**
     * Este metodo e invocado quando o usario utiliza o botao de "Extrato".
     * 
     * @param event
     */
    @Controller(events = EventType.QUICK_REPLY, pattern = "Extrato")
    public void replyExtatoReply(Event event) {
        reply(event, this.getMsgExtrato());
    }

    /**
     * Este metodo e invocado quando o usario envia as palavras(Extrato|extrato|estrato) o botao de
     * "Extrato".
     * 
     * @param event
     */
    @Controller(events = EventType.MESSAGE, pattern = "(Extrato|extrato|estrato|Estrato)")
    public void replyExtatoMessage(Event event) {
        reply(event, this.getMsgExtrato());
    }

    /**
     * This method is invoked when the user types "Show Buttons" or something which has "button" in
     * it as defined in the {@code pattern}.
     *
     * @param event
     */
    @Controller(events = EventType.MESSAGE, pattern = "(?i:button)")
    public void showButtons(Event event) {
        Button[] buttons = new Button[] {
            new Button().setType("web_url").setUrl("http://blog.ramswaroop.me").setTitle("JBot Docs"),
            new Button().setType("web_url").setUrl("https://goo.gl/uKrJWX").setTitle("Buttom Template") };
        reply(event, new Message().setAttachment(new Attachment().setType("template").setPayload(
            new Payload().setTemplateType("button").setText("These are 2 link buttons.").setButtons(buttons))));
    }

    /**
     * Show the github project url when the user says bye.
     *
     * @param event
     */
    @Controller(events = EventType.MESSAGE, pattern = "(?i)(bye|tchau|fui|falou|flw|até|até)")
    public void showGithubLink(Event event) {
        reply(event,
            new Message().setAttachment(new Attachment().setType("template")
                .setPayload(new Payload().setTemplateType("button").setText("Bye. Happy coding!")
                    .setButtons(new Button[] { new Button().setType("web_url").setTitle("View code")
                        .setUrl("https://github.com/ramswaroop/jbot") }))));
    }


    // Conversation feature of JBot

    /**
     * Type "setup meeting" to start a conversation with the bot. Provide the name of the next
     * method to be invoked in {@code next}. This method is the starting point of the conversation
     * (as it calls {@link IBot#startConversation(Event, String)} within it. You can chain methods
     * which will be invoked one after the other leading to a conversation.
     *
     * @param event
     */
    @Controller(pattern = "(?i)(setup meeting)", next = "confirmTiming")
    public void setupMeeting(Event event) {
        startConversation(event, "confirmTiming"); // start conversation
        reply(event, "Cool! At what time (ex. 15:30) do you want me to set up the meeting?");
    }

    /**
     * This method will be invoked after {@link FbBot#setupMeeting(Event)}. You need to call
     * {@link IBot#nextConversation(Event)} to jump to the next question in the conversation.
     *
     * @param event
     */
    @Controller(next = "askTimeForMeeting")
    public void confirmTiming(Event event) {
        reply(event,
            "Your meeting is set at " + event.getMessage().getText() + ". Would you like to repeat it tomorrow?");
        nextConversation(event); // jump to next question in conversation
    }

    /**
     * This method will be invoked after {@link FbBot#confirmTiming(Event)}. You can call
     * {@link IBot#stopConversation(Event)} to end the conversation.
     *
     * @param event
     */
    @Controller(next = "askWhetherToRepeat")
    public void askTimeForMeeting(Event event) {
        if (event.getMessage().getText().contains("yes")) {
            reply(event, "Okay. Would you like me to set a reminder for you?");
            nextConversation(event); // jump to next question in conversation
        } else {
            reply(event, "No problem. You can always schedule one with 'setup meeting' command.");
            stopConversation(event); // stop conversation only if user says no
        }
    }

    /**
     * This method will be invoked after {@link FbBot#askTimeForMeeting(Event)}. You can call
     * {@link IBot#stopConversation(Event)} to end the conversation.
     *
     * @param event
     */
    @Controller
    public void askWhetherToRepeat(Event event) {
        if (event.getMessage().getText().contains("yes")) {
            reply(event, "Great! I will remind you tomorrow before the meeting.");
        } else {
            reply(event, "Okay, don't forget to attend the meeting tomorrow :)");
        }
        stopConversation(event); // stop conversation
    }

    public String getMsgExtrato() {
        return msgExtrato;
    }

    public void setMsgExtrato(String msgExtrato) {
        this.msgExtrato = msgExtrato;
    }
}
