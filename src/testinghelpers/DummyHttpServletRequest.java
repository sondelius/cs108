package testinghelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.Part;

/**
 * Dummy implementation of HttpServletRequest to use for testing purposes. Only
 * supports setParameter, getParameter, getParameterNames and getSession.
 * 
 * @author Kevin
 *
 */
@SuppressWarnings("deprecation")
public class DummyHttpServletRequest implements HttpServletRequest {
	private HashMap<String, String> fakeParams = new HashMap<String, String>();

	public void setParameter(String key, String value) {
		fakeParams.put(key, value);
	}

	@Override
	public String getParameter(String key) {
		return fakeParams.get(key);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new Vector<String>(fakeParams.keySet()).elements();
	}

	@Override
	public HttpSession getSession() {
		return new HttpSession() {
			private HashMap<String, Object> fakeAttrs = new HashMap<String, Object>();

			@Override
			public Enumeration<String> getAttributeNames() {
				return new Vector<String>(fakeAttrs.keySet()).elements();
			}

			@Override
			public Object getAttribute(String key) {
				return fakeAttrs.get(key);
			}

			public void setAttribute(String key, Object value) {
				fakeAttrs.put(key, value);
			}

			public void removeAttribute(String key) {
				fakeAttrs.remove(key);
			}

			public void setMaxInactiveInterval(int arg0) {
			}

			public void removeValue(String arg0) {
			}

			@Override
			public void putValue(String arg0, Object arg1) {
			}

			@Override
			public boolean isNew() {
				return false;
			}

			@Override
			public void invalidate() {
			}

			@Override
			public String[] getValueNames() {
				return null;
			}

			@Override
			public Object getValue(String arg0) {
				return null;
			}

			@Override
			public HttpSessionContext getSessionContext() {
				return null;
			}

			@Override
			public ServletContext getServletContext() {
				return null;
			}

			@Override
			public int getMaxInactiveInterval() {
				return 0;
			}

			@Override
			public long getLastAccessedTime() {
				return 0;
			}

			@Override
			public String getId() {
				return null;
			}

			@Override
			public long getCreationTime() {
				return 0;
			}
		};
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public int getContentLength() {
		return 0;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getLocalAddr() {
		return null;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		return null;
	}

	@Override
	public String getRemoteAddr() {
		return null;
	}

	@Override
	public String getRemoteHost() {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getScheme() {
		return null;
	}

	@Override
	public String getServerName() {
		return null;
	}

	@Override
	public int getServerPort() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
	}

	@Override
	public AsyncContext startAsync() {
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		return false;
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {

		return 0;
	}

	@Override
	public String getHeader(String arg0) {

		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {

		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {

		return null;
	}

	@Override
	public int getIntHeader(String arg0) {

		return 0;
	}

	@Override
	public String getMethod() {

		return null;
	}

	@Override
	public Part getPart(String arg0) throws IOException, IllegalStateException,
			ServletException {

		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException, IllegalStateException,
			ServletException {

		return null;
	}

	@Override
	public String getPathInfo() {

		return null;
	}

	@Override
	public String getPathTranslated() {

		return null;
	}

	@Override
	public String getQueryString() {

		return null;
	}

	@Override
	public String getRemoteUser() {

		return null;
	}

	@Override
	public String getRequestURI() {

		return null;
	}

	@Override
	public StringBuffer getRequestURL() {

		return null;
	}

	@Override
	public String getRequestedSessionId() {

		return null;
	}

	@Override
	public String getServletPath() {

		return null;
	}

	@Override
	public HttpSession getSession(boolean arg0) {

		return null;
	}

	@Override
	public Principal getUserPrincipal() {

		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {

		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {

		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {

		return false;
	}

	@Override
	public void login(String arg0, String arg1) throws ServletException {

	}

	@Override
	public void logout() throws ServletException {

	}

}
