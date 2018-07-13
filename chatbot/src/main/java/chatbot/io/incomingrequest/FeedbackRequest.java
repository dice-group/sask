package chatbot.io.incomingrequest;

public class FeedbackRequest{
	private String query;
	private Feedback feedback = Feedback.POSITIVE;
	public enum Feedback{
		POSITIVE,
		NEGATIVE
	};
	
	public void setQuery(String query) {
		this.query=query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void setFeedback(String feedback) {
		System.out.println("here");
		if(feedback.toLowerCase().equals("negative")) {
			this.feedback = Feedback.NEGATIVE;
		}	
	}
	
	public Feedback getFeedback() {
		return this.feedback;			
	}
}