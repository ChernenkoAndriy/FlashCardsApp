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

    public String checkUserWordChoice(Card originalCard, String aiGeneratedSentence, String userAttempt) {
        var client = chatClientBuilder.build();
        String sentenceWithUserAttempt = aiGeneratedSentence.replace("... [" + originalCard.getDefinition() + "]", userAttempt);

        String aiPrompt = "You are an English language expert and a fair evaluator. " +
                "A student was given the following sentence with a blank: " +
                "\"" + aiGeneratedSentence + "\"\n" +
                "The student attempted to fill the blank with the word: \"" + userAttempt + "\".\n" +
                "The original, correct word for this blank was: \"" + originalCard.getWord() + "\".\n\n" +
                "Evaluate the student's attempt. Consider the following:\n" +
                "1. Is \"" + userAttempt + "\" the same word (or a grammatically correct form) as the original word \"" + originalCard.getWord() + "\"?\n" +
                "2. Is the sentence with the student's word (" + sentenceWithUserAttempt + ") grammatically correct?\n" +
                "3. Does \"" + userAttempt + "\" fit the meaning implied by the original definition ('" + originalCard.getDefinition() + "') in the sentence context?\n\n" +
                "Respond in one of the following ways:\n" +
                "- If the user's word is exactly the original word (or a correctly inflected form) and the sentence is grammatically perfect with it: " +
                "  \"The sentence is correct.\"\n" +
                "- If the user's word is not correct (either wrong word or wrong form), or if the sentence is grammatically incorrect with it: " +
                "  \"Not quite. The correct word is '" + originalCard.getWord() + "'. Your sentence with your word was: '" + sentenceWithUserAttempt + "'. " +
                "  The correct sentence would be: '" + aiGeneratedSentence.replace("... [" + originalCard.getDefinition() + "]", originalCard.getWord()) + "'. " +
                "  Explanation: [Brief explanation of why the user's word was incorrect, focusing on grammar or meaning incompatibility, or why a different form of the original word is needed].\"" +
                "  For example, if the original word was 'treasure' and the user put 'treasures', and the sentence needed 'treasure': " +
                "  \"Not quite. The correct word is 'treasure'. Your sentence with your word was: 'Stories about pirates often include a search for buried treasures.'. " +
                "  The correct sentence would be: 'Stories about pirates often include a search for buried treasure.'. " +
                "  Explanation: While 'treasures' is a valid word, the sentence context implies a singular 'treasure' (valuable thing) as a mass noun here, or if the original intended a singular noun.";

        return client.prompt(aiPrompt)
                .call()
                .content();
    }
}
