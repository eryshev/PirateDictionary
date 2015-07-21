package org.eapps.piratedictionary.persistence.entity;

/**
 * Represents a Term stored in the database.
 * Tuple (name, author) acts like a primary key.
 * The field name_na is generally the same as the field name,
 * it's used for exact value search in ElasticSearch, for more information see:
 * http://www.elastic.co/guide/en/elasticsearch/guide/current/_exact_values_versus_full_text.html
 * Created by eryshev-alexey on 08/07/15.
 */
public class Term {
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

    /** Helper class to remove a term by primary key combination
     * Primary Key for now is the tuple: (name, author)
     */
    public static class PrimaryKey {
        public String name;
        public String author;

        PrimaryKey() {
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getAuthor() {
            return author;
        }
    }

    private String name;
    private String name_na;
    private String definition;
    private String example;
    private String author;
    private String date;
    private Score score;

    public Term() {
    }

    public Term(String name_na, String name, String definition, String example, String author, String date, Integer positive, Integer negative) {
        this.name_na = name_na;
        this.name = name;
        this.definition = definition;
        this.example = example;
        this.author = author;
        this.date = date;
        this.score = new Score(positive, negative);
    }

    public String getName_na() {
        return name_na;
    }

    public void setName_na(String name_na) {
        this.name_na = name_na;
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
