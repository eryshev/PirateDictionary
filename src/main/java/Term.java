import java.util.Collection;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class Term {
    private String name;
    private Collection<String> definition;
    private String example;
    private String author;
    private String date;
    private Integer posScore;
    private Integer negScore;

    public Term(String name, Collection<String> definition, String example, String author, String date, Integer posScore, Integer negScore) {
        this.name = name;
        this.definition = definition;
        this.example = example;
        this.author = author;
        this.date = date;
        this.posScore = posScore;
        this.negScore = negScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getDefinition() {
        return definition;
    }

    public void setDefinition(Collection<String> definition) {
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

    public Integer getPosScore() {
        return posScore;
    }

    public void setPosScore(Integer posScore) {
        this.posScore = posScore;
    }

    public Integer getNegScore() {
        return negScore;
    }

    public void setNegScore(Integer negScore) {
        this.negScore = negScore;
    }

    @Override
    public String toString() {
        return "Term{" +
                "name='" + name + '\'' +
                ", definition='" + definition + '\'' +
                ", example='" + example + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", posScore=" + posScore +
                ", negScore=" + negScore +
                '}';
    }
}
