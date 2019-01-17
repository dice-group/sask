
package chatbot.core.handlers.qa.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Query {

    @SerializedName("sparql")
    @Expose
    private String sparql;

    public String getSparql() {
        return sparql;
    }

    public void setSparql(String sparql) {
        this.sparql = sparql;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("sparql", sparql).toString();
    }

}
