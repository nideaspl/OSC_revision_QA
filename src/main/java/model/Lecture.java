package model;

import java.util.List;

public class Lecture {
    private final String title;
    private final List<Question> questions;

    public Lecture(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
