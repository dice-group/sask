
package chatbot.core.handlers.qa.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Answer_ {

    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("results")
    @Expose
    private Results results;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("head", head).append("results", results).toString();
    }

}
