package com.example.application;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Scanner; // Import Scanner for user input

@SpringBootApplication
public class GeniniTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeniniTestApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ChatClient.Builder builder) {
        return args -> {
            var client = builder.build();
            Scanner scanner = new Scanner(System.in); // Create a Scanner object

            System.out.println("----------------------------------------------");
            System.out.println("English Sentence Corrector (Type 'exit' to quit)");
            System.out.println("----------------------------------------------");

            while (true) {
                System.out.print("\nEnter your sentence in English: ");
                String userSentence = scanner.nextLine(); // Read user input

                if ("exit".equalsIgnoreCase(userSentence.trim())) {
                    System.out.println("Exiting the application. Goodbye!");
                    break; // Exit the loop if user types 'exit'
                }

                if (userSentence.trim().isEmpty()) {
                    System.out.println("Please enter a sentence.");
                    continue; // Ask for input again if it's empty
                }

                // Construct the prompt for the AI
                String aiPrompt = "Act as a highly accurate English grammar and spelling checker. " +
                        "Analyze the following sentence for any grammatical errors, spelling mistakes, " +
                        "or awkward phrasing. If it's correct, say 'The sentence is correct.' " +
                        "If there are errors, provide the corrected sentence and a brief explanation " +
                        "of the changes. Only provide the corrected sentence if changes are made. " +
                        "Sentence to check: \"" + userSentence + "\"";

                System.out.println("Checking your sentence...");
                try {
                    String response = client.prompt(aiPrompt)
                            .call()
                            .content();
                    System.out.println("\nAI's feedback:\n" + response);
                } catch (Exception e) {
                    System.err.println("An error occurred while communicating with the AI: " + e.getMessage());
                    System.err.println("Please check your API key and network connection.");
                }
                System.out.println("----------------------------------------------");
            }

            scanner.close(); // Close the scanner when done
        };
    }
}