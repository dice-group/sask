
package chatbot.core.handlers.qa.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Head {

    @SerializedName("vars")
    @Expose
    private List<String> vars = null;

    public List<String> getVars() {
        return vars;
    }

    public void setVars(List<String> vars) {
        this.vars = vars;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vars", vars).toString();
    }

}
