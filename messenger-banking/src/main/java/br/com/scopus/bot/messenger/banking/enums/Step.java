package br.com.scopus.bot.messenger.banking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Step {

    SAUDACAO("text.messenger.saudacao"),
    MENU("text.messenger.menu"),
    SEU_SALDO("text.messenger.seu.saldo"),
    MAIS_ALGUMA_COISA("text.messenger.mais.alguma.coisa"),
    FINALIZAR_ATENDIMENTO("text.messenger.finalizar.atendimento"),
    NUMERO_NAO_CADASTRADO("text.messenger.numero.nao.cadastrado"),
    NAO_ENTENDI("text.messenger.nao.entendi"),
    AGUARDE("text.messenger.aguarde"),
    EXTRATO("text.messenger.extrato"),
    RECARGA_VALORES("text.messenger.recarga.valores"),
    RECARGA_CHAVE_SEGURANCA("text.messenger.recarga.chave.seguranca");
    
    @Getter
    String text;
    
}