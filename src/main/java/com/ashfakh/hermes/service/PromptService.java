package com.ashfakh.hermes.service;


import com.ashfakh.hermes.dto.PromptDTO;
import com.ashfakh.hermes.dto.SessionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    @Autowired
    private OpenAIAPIService openAiService;

    public PromptDTO getPrompt(String query, SessionDTO sessionDTO) {
        String lastMessage = "";
        try {
            if(sessionDTO.getChatHistory().size() > 1)
                lastMessage = sessionDTO.getChatHistory().get(sessionDTO.getChatHistory().size() - 2).replace("user:", "");
        } catch (Exception e) {
            System.err.println("Error getting last message: " + e.getMessage());
        }
        String useCaseJson = openAiService.getUseCase(query,lastMessage, getUseCasePrompt());
        ObjectMapper objectMapper = new ObjectMapper();
        String useCase = "Other";
        try {
            System.out.println("useCase: " + useCaseJson);
            JsonNode jsonObject = objectMapper.readTree(useCaseJson);
            useCase = jsonObject.get("use_case").asText();

        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        }

        return switch (useCase) {
            case "ContentGeneration" -> getContentGenerationPrompt(useCase);
            case "InformationRetrieval" -> getInformationRetrievalPrompt(useCase);
            case "ProblemSolving" -> getProblemSolvingPrompt(useCase);
            case "FollowUp" -> getFollowUpPrompt(sessionDTO);
            default -> getOtherPrompt("Other");
        };
    }

    public String getUseCasePrompt() {
        return "You are a query classifier who outputs json only and your job is to classify any message into the following different categories.\n" +
                "Followup: The message is related to the previous query and is a logical continuity to the previous message or query.\n" +
                "Reaction: The message is a positive or negative reaction from the user in the form of gratitude or repulsion\n" +
                "Content generation: The user is asking to generate a creative content piece like essay, poem, SoP etc\n" +
                "Information retrieval: The user is asking for some information or explanation regarding any topic or fact.\n" +
                "Problem solving: The user is asking for help with solving a problem which can be mathematical in nature\n" +
                "Rules: As a classifier, you will follow the classification structure as follows:\n" +
                "1. You will only classify the latest message comparing it with the previous messages.\n" +
                "2. First you will classify it to be follow up or a reaction message.\n" +
                "3. If it is not a followup or a reaction message, you will proceed to classify between Content generation, information retrieval and problem solving\n" +
                "4. You will only output a json object and nothing else with type as 'use_case' and value as the category. For example, {\"use_case\":\"ContentGeneration\"}";
    }

    public PromptDTO getContentGenerationPrompt(String useCase) {
        PromptDTO promptDTO = new PromptDTO();
        promptDTO.setUseCase(useCase);
        promptDTO.setPrompt("You're helping the user with creative content generation and review. You're given a query and you've to generate content for it or review the given content. " +
                " Rules for Content generation and review:\n" +
                " 1. When prompted a question to generate content, you will ask for a topic of choice if not provided \n" +
                " 2. You will also ask for the grade level politely only if the information is not already available. \n" +
                "3. If you are not clear with the differentiation between generation and review,only then, you will ask the user for the information on differentiation.\n"+
                "If you have to generate content:\n"+
                "1. You should generate the requested content in a structured manner with relevant headings for each section\n"+
                "If you are asked to review content: \n"+
                " 1. You will ask for the content that needs to be \n" +
                " reviewed and the context of where this content will be submitted to help \n" +
                " accordingly if the information is not already present.\n" +
                "2. You will give a relevant scoring out of 10, pointed reasoning for the rating and ways to improve it\n");
        return promptDTO;
    }

    public PromptDTO getInformationRetrievalPrompt(String useCase) {
        PromptDTO promptDTO = new PromptDTO();
        promptDTO.setUseCase(useCase);
        promptDTO.setPrompt("You're helping the user with information retrieval. You're given a query and you've to retrieve information for it. " +
                "Rules for Information retrieval:\n" +
                " 1. You will ask for what information the user needs and start answering\n" +
                " the question based on your professional calibre.\n" +
                " 2. When ending the explanation you will ask if the provided information \n" +
                " was clear or if a more in depth or simpler explanation is required." +
                " 3. You will retrieve information only to educate the user in any aspect they want and support career aspirations\n" +
                " 5. Your internal knowledge only limits till the date of July 2021.\n " +
                " 6. When asked about current affairs, then you must always reply with the prefix - 'As of July 2021' and suffix 'Unfortunately my knowledge cut off is July 2021' rephrased in a polite fashion \n" +
                " 7. You must politely ask the user to not use you for current affairs post the answer of current affairs.");
        return promptDTO;

    }

    public PromptDTO getProblemSolvingPrompt(String useCase) {
        PromptDTO promptDTO = new PromptDTO();
        promptDTO.setUseCase(useCase);
        promptDTO.setPrompt("You're helping the user with problem solving. You're given a query and you've to solve the problem for it. " +
                "Rules for Problem solving:\n" +
                " 1. When ending the explanation you will ask if the provided information \n" +
                "    was clear or if a more in depth or simpler explanation is required." +
                " 2. You will solve problems only to educate the user in any aspect they want and support career aspirations\n" +
                " 3. If you're confident about the solved problem, you must defend your answer.\n ");
        return promptDTO;
    }

    public PromptDTO getFollowUpPrompt(SessionDTO sessionDTO) {
        String currentUseCase = sessionDTO.getUseCase();
        return switch (currentUseCase) {
            case "ContentGeneration" -> getContentGenerationPrompt(currentUseCase);
            case "InformationRetrieval" -> getInformationRetrievalPrompt(currentUseCase);
            case "ProblemSolving" -> getProblemSolvingPrompt(currentUseCase);
            default -> getOtherPrompt("Other");
        };
    }

    public PromptDTO getOtherPrompt(String useCase) {
        PromptDTO promptDTO = new PromptDTO();
        promptDTO.setUseCase(useCase);
        promptDTO.setPrompt("You're an education bot, if the question is not helping the user with educational needs, you should politely decline to answer.");
        return promptDTO;
    }


}
