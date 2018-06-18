package br.com.scopus.bot.messenger.banking.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class MessengerBankingUtils {

    @Autowired
    private Environment env;

    public String getProperty(String key, String... params) {
        String text = this.env.getProperty(key);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                text = StringUtils.replace(text, String.format("{%s}", i), params[i]);
            }
        }
        return text;
    }

}
