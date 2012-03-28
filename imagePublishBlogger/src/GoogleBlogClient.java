import interfaces.IImagePublish;


public class GoogleBlogClient implements IImagePublish {

	public GoogleBlogClient() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void publish(String title, String content) {
		// TODO Auto-generated method stub
		System.out.println (title + content);
	}

}
