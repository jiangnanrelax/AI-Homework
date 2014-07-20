package HTTP;

public class HttpStatusCode {
	public final static int HTTP_OK = 200;
	public final static int HTTP_FOUND = 302;
	public final static int HTTP_MOVED_PERMANENTLY = 301;
	public final static int HTTP_UNAUTHORIZED = 401;
	public final static int HTTP_FORBIDDEN = 403;
	public final static int HTTP_NOT_FOUND = 404;
	public final static int HTTP_NOT_MODIFIED = 304;

	/** HTTP GET request **/
	public final static int GET = 1;

	/** HTTP POST request **/
	public final static int POST = 2;

	/** HTTP HEAD request **/
	public final static int HEAD = 3;
}
