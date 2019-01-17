
package chatbot.core.handlers.qa.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Question_ {

    @SerializedName("string")
    @Expose
    private String string;
    @SerializedName("language")
    @Expose
    private String language;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("string", string).append("language", language).toString();
    }

}
