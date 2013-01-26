package fr.um2.apicaller;

/**
 * Response of ower Api
 * 
 * @author bibouh
 * 
 */
public class ResponseApi<T> {
	String what;
	String type;
	String info;
	String details;

	public ResponseApi() {
		what = API_ERROR;
	}

	private static String API_SUCCESS = "success";
	private static String API_ERROR = "error";

	/**
	 * if the response is good or not
	 * 
	 * @return {@link Boolean}
	 */
	public boolean isOK() {
		return what.equals(API_SUCCESS);
	}

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return what + " : " + type + "\n " + info + "\n " + details;
	}

	T results;

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}
}
