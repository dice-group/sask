
package chatbot.core.handlers.qa.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Binding {

    @SerializedName("resource")
    @Expose
    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("resource", resource).toString();
    }

}
