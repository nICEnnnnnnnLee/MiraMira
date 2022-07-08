package top.nicelee.mirai.miramira.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class HttpRequestUtil {

	private static CookieManager defaultManager = new CookieManager();
	// Cookie管理
	CookieManager manager;

	public HttpRequestUtil() {
		this.manager = defaultManager;
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);
	}

	public HttpRequestUtil(CookieManager manager) {
		this.manager = manager;
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(manager);
	}

	/**
	 * @param headers
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection connect(HashMap<String, String> headers, String url, List<HttpCookie> listCookie)
			throws MalformedURLException, IOException {
		URL realUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);

		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
//			 System.out.println(entry.getKey() + ":" + entry.getValue());
			}
		}
		// 设置Cookie
		if (listCookie != null) {
			StringBuilder sb = new StringBuilder();
			for (HttpCookie cookie : listCookie) {
				sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
			}
			String cookie = sb.toString();
			if (cookie.endsWith("; ")) {
				cookie = cookie.substring(0, cookie.length() - 2);
			}
			// System.out.println(cookie);
			conn.setRequestProperty("Cookie", cookie);
		}
		// conn.connect();
		return conn;
	}

	public String getContent(String url, HashMap<String, String> headers) {
		return getContent(url, headers, null);
	}

	/**
	 * do a Http Get
	 * 
	 * @param url
	 * @param headers
	 * @return content, mostly a html page
	 * @throws IOException
	 */
	public String getContent(String url, HashMap<String, String> headers, List<HttpCookie> listCookie) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			HttpURLConnection conn = connect(headers, url, listCookie);
			conn.connect();

			String encoding = conn.getContentEncoding();
			InputStream ism = conn.getInputStream();
			if (encoding != null && encoding.contains("gzip")) {// 首先判断服务器返回的数据是否支持gzip压缩，
				// System.out.println(encoding);
				// 如果支持则应该使用GZIPInputStream解压，否则会出现乱码无效数据
				ism = new GZIPInputStream(ism);
			} else if (encoding != null && encoding.contains("deflate")) {
				ism = new InflaterInputStream(ism, new Inflater(true));
			}

			in = new BufferedReader(new InputStreamReader(ism, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				// line = new String(line.getBytes(), "UTF-8");
				result.append(line).append("\r\n");
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// printCookie(manager.getCookieStore());
		return result.toString();
	}

	/**
	 * do a Http Post Not Worked with http stream with Deflate
	 * 
	 * @param url
	 * @param headers
	 * @param param
	 * @return
	 */
	public String postContent(String url, HashMap<String, String> headers, String param) {
		return postContent(url, headers, param, null);
	}

	/**
	 * do a Http Post Not Worked with http stream with Deflate
	 * 
	 * @param url
	 * @param headers
	 * @param param
	 * @param iCookies 可以为null
	 * @return
	 */
	public String postContent(String url, HashMap<String, String> headers, String param, List<HttpCookie> listCookie) {
		ByteArrayOutputStream out;
		InputStream in = null;
		try {
			HttpURLConnection conn = connect(headers, url, listCookie);
			// 设置参数
			conn.setDoOutput(true); // 需要输出
			conn.setDoInput(true); // 需要输入
			conn.setUseCaches(false); // 不允许缓存
			conn.setRequestMethod("POST"); // 设置POST方式连接
			conn.connect();

			// 建立输入流，向指向的URL传入参数
			OutputStream dos = (conn.getOutputStream());
			dos.write(param.getBytes());
			dos.flush();
			dos.close();

			String encoding = conn.getContentEncoding();
			in = conn.getInputStream();
			// 判断服务器返回的数据是否支持gzip压缩
			if (encoding != null && encoding.contains("gzip")) {
				in = new GZIPInputStream(conn.getInputStream());
			}
			byte[] buffer = new byte[2048];
			out = new ByteArrayOutputStream();
			int len = in.read(buffer);
			while (len > 0) {
				out.write(buffer, 0, len);
				len = in.read(buffer);
			}
			return out.toString("UTF-8");
			// printCookie(manager.getCookieStore());
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			// e.printStackTrace();
			return "";
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	// 打印cookie信息
	public static String printCookie(CookieStore cookieStore) {
		List<HttpCookie> listCookie = cookieStore.getCookies();
		StringBuilder sb = new StringBuilder();
		for (HttpCookie httpCookie : listCookie) {
//			System.out.println("--------------------------------------" + httpCookie.toString());
			sb.append(httpCookie.toString()).append("; ");
			System.out.println("class      : " + httpCookie.getClass());
			System.out.println("comment    : " + httpCookie.getComment());
			System.out.println("commentURL : " + httpCookie.getCommentURL());
			System.out.println("discard    : " + httpCookie.getDiscard());
			System.out.println("maxAge     : " + httpCookie.getMaxAge());
			System.out.println("name       : " + httpCookie.getName());
			System.out.println("portlist   : " + httpCookie.getPortlist());
			System.out.println("secure     : " + httpCookie.getSecure());
			System.out.println("version    : " + httpCookie.getVersion());
			System.out.println("domain     : " + httpCookie.getDomain());
			System.out.println("path       : " + httpCookie.getPath());
			System.out.println("value      : " + httpCookie.getValue());
			System.out.println("httpCookie : " + httpCookie);
		}
		return sb.toString();
	}

	public boolean download(String url, File dstFile, HashMap<String, String> headers) {
		InputStream inn = null;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(dstFile, "rw");
			// 获取下载进度
			long offset = 0;
			offset = modifyHeaderMapByDownloaded(headers, raf, dstFile, offset);
			// 开始下载
			HttpURLConnection conn = connect(headers, url, null);
			conn.connect();

			inn = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int lenRead = inn.read(buffer);
			while (lenRead > -1) {
				raf.write(buffer, 0, lenRead);
				lenRead = inn.read(buffer);
			}
			raf.close();
			return true;
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
			return false;
		} finally {
			closeQuietly(inn);
			closeQuietly(raf);
		}
	}

	/**
	 * 获取已经下载字节数，并修饰Header
	 * 
	 * @param headers
	 * @param raf
	 * @param fileDownloadPart
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	protected long modifyHeaderMapByDownloaded(HashMap<String, String> headers, RandomAccessFile raf,
			File fileDownloadPart, long offset) throws IOException {
		headers.remove("range");
		if (fileDownloadPart.exists() && fileDownloadPart.length() > 0) {
			offset = fileDownloadPart.length();
			headers.put("range", "bytes=" + offset + "-");// + (total - 1)
			raf.seek(offset);
		}
		return offset;
	}

	public static void close(Object resource) throws IOException, NoSuchMethodException, SecurityException {
		if (resource == null)
			return;
		if (resource instanceof Closeable) {
			((Closeable) resource).close();
		} else {
			System.err.println(resource.getClass().getName() + ": 尝试利用反射调用close()方法");
			try {
				Method method = resource.getClass().getDeclaredMethod("close");
				try {
					method.invoke(resource);
				} catch (Exception e) {
				}
			} catch (NoSuchMethodException | SecurityException e1) {
				Method method = resource.getClass().getMethod("close");
				try {
					method.invoke(resource);
				} catch (Exception e) {
				}
			}
		}
	}

	public static void closeQuietly(Object resource) {
		try {
			close(resource);
		} catch (NoSuchMethodException | SecurityException | IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
