# Hermes

Hermes is a powerful, multichannel LLM chatbot framework crafted in Java. It's the engine that empowers [Drona](https://getdrona.ai), an online tutor that serves over 1000 students via WhatsApp and Telegram, all at an incredibly efficient infrastructure cost.

## 🌟 Features

- ✅ Multichannel Support: Ready to work on multiple platforms like WhatsApp, Telegram, etc.
- ✅ Media Support: Seamlessly handle media files.

## 🚀 Usage

1. **Clone the Repository**

   ```
   git clone [repository_url]
   ```

2. **Set Up Your Bot**

3. **Implementation**

   ```java
   @Service
   public class EducationBot extends BaseBot {

       public EducationBot(OpenAIAPIService openAIService, SummaryService summaryService, SessionService sessionService, RedisTemplate<String, List<String>> redisTemplate, ExecutorService executorService, PromptService promptService, UserService userService) {
           super(openAIService, summaryService, sessionService, redisTemplate, executorService, promptService, userService);
       }

       @Override
       public String getBotName() {
           return "Educator";
       }

       public String getPersona() {
           return "You are Educator, an academic instructor who answers any questions asked by\n" +
                   "the users to educate them in a descriptive yet simple manner.";
       }
   }
   ```

   Note: The bot is initialized automatically as a bean.

4. **For Telegram**

   Make sure to provide the necessary configurations:

   ```
   telegram.bot.username=ABC
   telegram.bot.token=123
   ```

   And then, use the following code:

   ```java
   TestTelegram testTelegram = context.getBean(TestTelegram.class);
   try {
       TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
       botsApi.registerBot(testTelegram);
   } catch (TelegramApiException e) {
       e.printStackTrace();
   }
   ```

5. **For WhatsApp**

   Provide your phone number ID and token. Make sure to also set up a controller for callbacks from the WhatsApp cloud API:

   ```
   whatsapp.token=ABC
   whatsapp.phonenumber.id=123
   ```

---

Remember to replace `[repository_url]` with the actual URL of the Hermes repository.

🔧 To-Do

Setup LLM Load Balancer

To ensure efficient handling of multiple requests, setting up an LLM load balancer is crucial. Detailed instructions and configurations related to the LLM load balancer will be added soon.

Setup Vector Memory for LLM

To ensure that the LLM model is able to retrieve context for a conversation, a vector memory is required. Detailed instructions and configurations related to the vector memory will be added soon.