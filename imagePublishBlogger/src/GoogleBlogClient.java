import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import interfaces.IImagePublish;


public class GoogleBlogClient implements IImagePublish {
	private String userName, userPassword ;
	
	private final String METAFEED_URL = "http://www.blogger.com/feeds/default/blogs" ;
	private final String FEED_URI_BASE = "http://www.blogger.com/feeds" ;
	private final String POSTS_FEED_URI_SUFFIX = "/posts/default" ;
	private String feedUri;
	private String author;
	private BloggerService service ;

	public GoogleBlogClient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void publish(String title, String content) {
		// TODO Auto-generated method stub
		try 
		{
			run (title, content);
			
		} catch (ServiceException s) {
			throw new RuntimeException ("") ;
		} catch (IOException e) {
			throw new RuntimeException ("") ;
		}
	} 

	/**
	 * Definit l'adresse du proxy
	 * @param proxyHost l'adresse du proxy
	 */
	@Override
	public void setProxyHost(String proxyHost) {
		// TODO Auto-generated method stub
		System.setProperty("https.proxyHost", proxyHost);
		System.setProperty("http.proxyHost", proxyHost);
	}

	/**
	 * Definit le port du proxy
	 * @param proxyPort le port du proxy
	 */
	@Override
	public void setProxyPort(String proxyPort) {
		// TODO Auto-generated method stub
		if (!proxyPort.equals("0")){
			System.setProperty("https.proxyPort", proxyPort);
			System.setProperty("http.proxyPort", proxyPort);
		}
	}
	
	private Entry createPost(String title, String content , Boolean isDraft)
		      throws ServiceException, IOException {
		    // Create the entry to insert
		    Entry myEntry = new Entry();
		    myEntry.setTitle(new PlainTextConstruct(title));
		    myEntry.setContent(new PlainTextConstruct(content));
		    
		    Person author = new Person(userName , null, userName);
		    myEntry.getAuthors().add(author);
		    myEntry.setDraft(isDraft);

		    // Ask the service to insert the new entry
		    URL postUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
		    return  service.insert(postUrl, myEntry);
		  }
	
	private void run(String title, String content) throws ServiceException, IOException {
		    // Get the blog ID from the metatfeed.
		    String blogId = getBlogId();
		    feedUri = FEED_URI_BASE + "/" + blogId;

		    // Demonstrate how to publish a public post.
		    Entry publicPost = createPost(title, content, false);
			 System.out.println("Successfully created public post: "
			  + publicPost.getTitle().getPlainText());
		  }
	
	private  String getBlogId() throws ServiceException, IOException {
		// Get the metafeed
		final URL feedUrl = new URL(METAFEED_URL);
		Feed resultFeed = service.getFeed(feedUrl, Feed.class);

		// If the user has a blog then return the id (which comes after 'blog-')
		if (resultFeed.getEntries().size() > 0) {
			Entry entry = resultFeed.getEntries().get(0);
			return entry.getId().split("blog-")[1];
		}
		throw new IOException("User has no blogs!");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	    JTextField utilisateur = new JTextField();
	    JTextField auteur = new JTextField();
	    JPasswordField passe = new JPasswordField();
	    
	    int s = JOptionPane.showOptionDialog(null, 
	      new Object[] {
	    		"Votre nom :", utilisateur,
	    		"Mot de passe :", passe, 
	    		"Nom pour les post :", auteur},
	      "Connexion google blog" ,
	      JOptionPane.OK_CANCEL_OPTION,
	      JOptionPane.QUESTION_MESSAGE, null, null, null); 
	    
	    if (s == JOptionPane.OK_OPTION) {
	    	userName = utilisateur.getText() ;
	    	author = auteur.getText() ;
	    	userPassword = passe.getText() ;
	    	System.out.println(userName + userPassword) ;
	    }
	    else
	    	throw new RuntimeException("Pas d'info de connexion");
		
	    service=new BloggerService(author);
	    // Authenticate using ClientLogin
	    try {
			service.setUserCredentials(userName, userPassword);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("impossible de se connecter") ;
		}
   } 
}
