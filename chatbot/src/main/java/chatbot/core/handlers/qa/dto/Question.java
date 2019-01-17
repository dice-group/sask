
package chatbot.core.handlers.qa.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Question {

    @SerializedName("question")
    @Expose
    private List<Question_> question = null;
    @SerializedName("answertype")
    @Expose
    private String answertype;
    @SerializedName("query")
    @Expose
    private Query query;
    @SerializedName("answers")
    @Expose
    private List<Answer_> answers = null;
    @SerializedName("id")
    @Expose
    private String id;

    public List<Question_> getQuestion() {
        return question;
    }

    public void setQuestion(List<Question_> question) {
        this.question = question;
    }

    public String getAnswertype() {
        return answertype;
    }

    public void setAnswertype(String answertype) {
        this.answertype = answertype;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public List<Answer_> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer_> answers) {
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("question", question).append("answertype", answertype).append("query", query).append("answers", answers).append("id", id).toString();
    }

}
