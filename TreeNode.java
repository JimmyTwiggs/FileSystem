import java.util.*;
import java.io.*;

public class TreeNode {

	public Vector<TreeNode> below = new Vector<TreeNode>();
	private String path;
	private boolean IsFile;
	private File myfile;
	

	public TreeNode(String a, boolean b, TreeNode here)
	{
		path = a;
		IsFile = b;
		if(IsFile == false){
			if(here != null){
				if(path.equals("..")){
					below.addElement(here);
				}
				else{
					below.addElement(new TreeNode("..", false, here));
				}
			}
		}
		else
		{
			//This file path MUST be specified to the exact directory where the files will be stored.
			//		Its different based on operating system / HD vs external storage
			
			myfile = new File("/Users/James/Documents/workspace/Files/src/files/" + path);
			try {
				myfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myfile.deleteOnExit();
		}
	}
	
	public TreeNode(String a, boolean b)
	{
		path = a;
		IsFile = b;
		
	}

	public TreeNode()
	{
		path = "";
		IsFile = false;
	}
	
	public String getPath(){
		return path;
	}
	
	public boolean getIsFile(){
		return IsFile;
	}
	
	public File getFile(){
		return myfile;
	}
	
	public void setPath(String newname){
		path = newname;
		File a = new File("/Users/James/Documents/workspace/Files/src/files/" + newname);
		myfile.renameTo(a);
	}
}