import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class Question {
        String question;
        String answer;

        Question(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("src/main/resources/QA.json"));

            // Load lectures
            List<String> lectureTitles = new ArrayList<>();
            List<List<Question>> lectures = new ArrayList<>();
            for (JsonNode lecture : root.get("lectures")) {
                lectureTitles.add(lecture.get("title").asText());
                List<Question> questions = new ArrayList<>();
                for (JsonNode questionNode : lecture.get("questions")) {
                    String question = questionNode.get("question").asText();
                    String answer = questionNode.get("answer").asText();
                    questions.add(new Question(question, answer));
                }
                lectures.add(questions);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Select a lecture to test:");
            for (int i = 0; i < lectureTitles.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, lectureTitles.get(i));
            }

            int lectureChoice;
            while (true) {
                System.out.print("Enter the number of the lecture: ");
                lectureChoice = scanner.nextInt();
                if (lectureChoice > 0 && lectureChoice <= lectureTitles.size()) {
                    break;
                }
                System.out.println("Invalid choice. Try again.");
            }

            List<Question> selectedLecture = lectures.get(lectureChoice - 1);

            System.out.println("Select question order:");
            System.out.println("1. Sequential");
            System.out.println("2. Randomized");

            int orderChoice;
            while (true) {
                System.out.print("Enter your choice: ");
                orderChoice = scanner.nextInt();
                if (orderChoice == 1 || orderChoice == 2) {
                    break;
                }
                System.out.println("Invalid choice. Try again.");
            }

            if (orderChoice == 2) {
                Collections.shuffle(selectedLecture);
            }

            scanner.nextLine(); // Consume newline left by nextInt()

            for (Question question : selectedLecture) {
                System.out.println("Question: " + question.question);
                System.out.print("Press Enter to see the answer...");
                scanner.nextLine();
                System.out.println("Answer: " + question.answer);
                System.out.println();
            }

            System.out.println("Quiz complete! Thanks for participating.");

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }
}
