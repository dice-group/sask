
package chatbot.core.handlers.qa.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Results {

    @SerializedName("bindings")
    @Expose
    private List<Binding> bindings = null;

    public List<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bindings", bindings).toString();
    }

}
