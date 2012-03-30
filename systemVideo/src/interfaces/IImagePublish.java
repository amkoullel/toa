package interfaces;

public interface IImagePublish {
	void publish (String title , String content) ;
	void init () ;
	void setProxyHost (String proxyHost);
	void setProxyPort (String proxyPort);
}
