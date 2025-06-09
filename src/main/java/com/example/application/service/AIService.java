package com.example.application.service;

import com.example.application.data.Card;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient.Builder chatClientBuilder;

    public AIService(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    public String checkSentenceWithCardWord(Card card, String userSentence) {
        var client = chatClientBuilder.build();

        String aiPrompt = "Act as a highly accurate English grammar and spelling checker. " +
                "Analyze if the following sentence correctly uses the word '" + card.getWord() + "'. " +
                "Check for grammatical errors, spelling mistakes, or incorrect usage of the word. " +
                "If the sentence is correct, say 'The sentence is correct.' " +
                "If there are errors, provide the corrected sentence and a brief explanation " +
                "of the changes. Focus particularly on the usage of the word '" + card.getWord() + "'. " +
                "Sentence to check: \"" + userSentence + "\"";

        return client.prompt(aiPrompt)
                .call()
                .content();
    }

    public String createSentence(Card card) {
        var client = chatClientBuilder.build();

        String aiPrompt = "You are a highly accurate English grammar and vocabulary teacher. " +
                "Your task is to create *one single sentence* that serves as a vocabulary test question. " +
                "This sentence must use a form of the word '" + card.getWord() + "' in a way that its meaning " +
                "directly corresponds to '" + card.getDefinition() + "'. " +
                "The sentence should be grammatically correct and clearly illustrate the given definition. " +
                "Crucially, replace the word '" + card.getWord() + "' (or its chosen form) with '...' and then " +
                "immediately follow it with the definition in brackets, like this: `... [definition]`. " +
                "Ensure the sentence contextually implies the definition provided. " +
                "Do NOT use the word '" + card.getWord() + "' anywhere else in the sentence, only replaced by '...'. " +
                "Example for 'apple' meaning 'fruit': 'She carefully bit into the crisp, red ... [fruit].' " +
                "Example for 'treasure' meaning 'very valuable thing': 'Stories about pirates often include a search for buried ... [very valuable thing].' " +
                "Now, create a sentence for the word '" + card.getWord() + "' meaning '" + card.getDefinition() + "':";

        return client.prompt(aiPrompt)
                .call()
                .content();
    }



}