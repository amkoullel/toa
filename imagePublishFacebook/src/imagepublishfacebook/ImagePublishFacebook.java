package imagepublishfacebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

import interfaces.IImagePublish ;

public class ImagePublishFacebook implements IImagePublish {
	private String MY_ACCESS_TOKEN = "AAAFrJGaK4W4BAD4fIV5J2l4JPEFBc1aiBWmZB6ZCLIpH1E2OHKOsvJsZBenzRlmBJMBn9qSIytWpdPVu9ZAYGDyN12ystIh60t7nUr69YI6AxUZBS0a1W" ;
	private FacebookClient facebookClient ;
	
	public ImagePublishFacebook() {
		facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
	}

	@Override
	public void publish(String title, String content) {
		// TODO Auto-generated method stub
		
		System.out.println("Avant");
		
		FacebookType publishMessageResponse = 
			facebookClient.publish("me/feed", FacebookType.class,Parameter.with("message", title + content));
	

		System.out.println("apres");
		System.out.println("Published event ID: " + publishMessageResponse.getId());
	}

	/**
	 * Definit l'adresse du proxy
	 * @param proxyHost l'adresse du proxy
	 */
	@Override
	public void setProxyHost(String proxyHost) {
		// TODO Auto-generated method stub
		System.setProperty("https.proxyHost", proxyHost);
	}

	/**
	 * Definit le port du proxy
	 * @param proxyPort le port du proxy
	 */
	@Override
	public void setProxyPort(String proxyPort) {
		// TODO Auto-generated method stub
		System.setProperty("https.proxyPort", proxyPort);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		MY_ACCESS_TOKEN = "AAAFrJGaK4W4BAD4fIV5J2l4JPEFBc1aiBWmZB6ZCLIpH1E2OHKOsvJsZBenzRlmBJMBn9qSIytWpdPVu9ZAYGDyN12ystIh60t7nUr69YI6AxUZBS0a1W" ;
	}

}
