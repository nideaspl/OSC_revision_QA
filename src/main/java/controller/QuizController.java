package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Lecture;
import model.Question;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController {

    @FXML
    private ListView<String> lectureListView;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea questionTextArea;
    @FXML
    private TextArea answerTextArea;
    @FXML
    private Button showAnswerButton;
    @FXML
    private HBox buttonBox;
    @FXML
    private Label quizTitleLabel;

    private List<Lecture> lectures = new ArrayList<>();
    private List<Question> currentQuestions;
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        loadLectures();

        // Populate lecture titles into the ListView
        List<String> lectureTitles = new ArrayList<>();
        for (Lecture lecture : lectures) {
            lectureTitles.add(lecture.getTitle());
        }
        lectureListView.setItems(FXCollections.observableArrayList(lectureTitles));
    }

    @FXML
    public void handleStartQuiz() {
        int selectedLectureIndex = lectureListView.getSelectionModel().getSelectedIndex();
        if (selectedLectureIndex < 0) {
            showAlert("Error", "No lecture selected!", Alert.AlertType.ERROR);
            return;
        }

        // Start quiz
        currentQuestions = new ArrayList<>(lectures.get(selectedLectureIndex).getQuestions());
        Collections.shuffle(currentQuestions); // Shuffle questions
        currentIndex = 0;

        // Update GUI
        titleLabel.setVisible(false);
        lectureListView.setVisible(false);
        quizTitleLabel.setVisible(true);
        questionTextArea.setVisible(true);
        showAnswerButton.setVisible(true);
        buttonBox.setVisible(true);

        displayCurrentQuestion();
    }

    @FXML
    public void handleShowAnswer() {
        Question currentQuestion = currentQuestions.get(currentIndex);
        answerTextArea.setText(currentQuestion.getAnswer());
        answerTextArea.setVisible(true);
    }

    @FXML
    public void handleNextQuestion() {
        if (currentIndex < currentQuestions.size() - 1) {
            currentIndex++;
            displayCurrentQuestion();
        } else {
            showAlert("End of Quiz", "You have completed all questions!", Alert.AlertType.INFORMATION);
            resetView();
        }
    }

    @FXML
    public void handleFinishQuiz() {
        showAlert("Quiz Finished", "Thanks for participating!", Alert.AlertType.INFORMATION);
        resetView();
    }

    private void loadLectures() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("src/main/resources/QA.json"));

            for (JsonNode lectureNode : root.get("lectures")) {
                String title = lectureNode.get("title").asText();
                List<Question> questions = new ArrayList<>();
                for (JsonNode questionNode : lectureNode.get("questions")) {
                    String question = questionNode.get("question").asText();
                    String answer = questionNode.get("answer").asText();
                    questions.add(new Question(question, answer));
                }
                lectures.add(new Lecture(title, questions));
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to load lectures: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayCurrentQuestion() {
        Question currentQuestion = currentQuestions.get(currentIndex);
        questionTextArea.setText(currentQuestion.getQuestion());
        answerTextArea.clear();
        answerTextArea.setVisible(false);
    }

    private void resetView() {
        titleLabel.setVisible(true);
        lectureListView.setVisible(true);
        quizTitleLabel.setVisible(false);
        questionTextArea.setVisible(false);
        showAnswerButton.setVisible(false);
        buttonBox.setVisible(false);
        answerTextArea.setVisible(false);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
