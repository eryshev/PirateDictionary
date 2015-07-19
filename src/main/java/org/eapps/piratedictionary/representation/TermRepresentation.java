package org.eapps.piratedictionary.representation;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * It's a container for the representation of a Term
 * The {@link JacksonXmlRootElement} annotation : the root element of the
 * XML representation will be called "Term".
 * Created by eryshev-alexey on 16/07/15.
 */

@JacksonXmlRootElement(localName = "Term")
public class TermRepresentation {
    public static class Score {
        private Integer positive, negative;

        public Score() {
        }

        public Score(Integer positive, Integer negative) {
            this.positive = positive;
            this.negative = negative;
        }

        public Integer getPositive() {
            return positive;
        }

        public void setPositive(Integer positive) {
            this.positive = positive;
        }

        public Integer getNegative() {
            return negative;
        }

        public void setNegative(Integer negative) {
            this.negative = negative;
        }
    }

    private String name;
    private String definition;
    private String example;
    private String author;
    private String date;
    private Score score;

    public TermRepresentation() {
    }

    public TermRepresentation(String name,
                              String definition,
                              String example,
                              String author,
                              String date,
                              Integer positive,
                              Integer negative) {
        this.name = name;
        this.definition = definition;
        this.example = example;
        this.author = author;
        this.date = date;
        this.score = new Score(positive, negative);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}
