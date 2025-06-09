package com.example.application.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient.Builder chatClientBuilder;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    public String checkSentenceWithCardWord(String cardWord, String userSentence) {
        var client = chatClientBuilder.build();

        String aiPrompt = "Act as a highly accurate English grammar and spelling checker. " +
                "Analyze if the following sentence correctly uses the word '" + cardWord + "'. " +
                "Check for grammatical errors, spelling mistakes, or incorrect usage of the word. " +
                "If the sentence is correct, say 'The sentence is correct.' " +
                "If there are errors, provide the corrected sentence and a brief explanation " +
                "of the changes. Focus particularly on the usage of the word '" + cardWord + "'. " +
                "Sentence to check: \"" + userSentence + "\"";

        return client.prompt(aiPrompt)
                .call()
                .content();
    }
}