//Still To Do:
//	Aesthetics (not important yet)
//	Function that contains the switch statement and allows for arguments with commands
//	list function should display all elements by type and alphabetically 

import java.util.*;
import java.io.*;

public class FileSys {
	
	public static Scanner scan = new Scanner(System.in);

	////////////////////////////////////
	// Main
	////////////////////////////////////
	public static void main(String[] args)
	{
		FileSys files = new FileSys();
		
		File DirPath = new File("/Users/James/Documents/workspace/Files/src/files/");
		DirPath.mkdirs();
		DirPath.deleteOnExit();
		
		String command = "";
		boolean isDone = false;

		System.out.println("This is your file system.\nCurrent Location: root\nType 'help' for a list of valid commands.");

		while (isDone == false){
			System.out.println("Please enter a command: ");
			command = scan.nextLine();
		
			switch(command){
				//A case for each valid command.
				case "createfile": 
					System.out.println("Please enter a file name: ");
					command = scan.nextLine();
					files.createFile(command);
					break;
				case "delete":  
					System.out.println("Please enter a file or folder to delete: ");
					command = scan.nextLine();
					files.delete(command);
					break;
				case "read":  
					System.out.println("Please enter a file to read: ");
					command = scan.nextLine();
					files.read(command);
				    break;
				case "write":  
					System.out.println("Please enter a file to write: ");
					command = scan.nextLine();
					files.write(command);
				    break;
				case "createfolder": 
					System.out.println("Please enter a folder name: ");
					command = scan.nextLine();
					files.createFolder(command);
				    break;
				case "list":  
					files.list();
				    break;
				case "cd":  
					System.out.println("Please enter a folder name: ");
					command = scan.nextLine();
					files.cd(command);
				    break;
				case "rename":  
					System.out.println("Please enter a folder name: ");
					command = scan.nextLine();
					files.rename(command);
				    break;
				case "move": 
					System.out.println("Please enter the file to move: ");
					command = scan.nextLine();
					System.out.println("Please enter new filepath: ");
					String filepath = scan.nextLine();
					files.move(command, filepath);
				    break;
				case "copy": 
					System.out.println("Please enter a folder name: ");
					command = scan.nextLine();
					files.copy(command);
				    break;
				case "cut": 
					System.out.println("Please enter a folder name: ");
					command = scan.nextLine();
					files.cut(command);
				    break;
				case "paste": 
					files.paste();
					break;
				case "help":
					try {
						BufferedReader f = new BufferedReader(new FileReader("/Users/James/Documents/workspace/Files/src/help.txt"));
						String line = null;
						while((line = f.readLine()) != null){
							System.out.println(line);
						}
						//call close(); to close the file
						f.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e){
						e.printStackTrace();
					}	
					break;
				case "exit":
					return;
				default: System.out.println("Not a valid command.");
				    break;
			}
		}
		
		scan.close();
	}

	
	////////////////////////////////////
	// File System Class
	////////////////////////////////////
	
	public TreeNode root;
	public TreeNode here;
	public TreeNode copied;
	public Scanner File;
	public final String folderpath = "/Users/James/Documents/workspace/Files/src/files/";

	
	//Default Constructor
	public FileSys()
	{
		root = new TreeNode("root", false, null);
		here = root;
		here.below.addElement(new TreeNode("Docs", false, here));
		here.below.addElement(new TreeNode("Lib", false, here));
	}


	////////////////////////////////////
	// File Commands
	////////////////////////////////////
	public void createFile(String name)
	{
		if(here == null){
			root = new TreeNode("root", false, null);
			here = root;
			here.below.addElement(new TreeNode(name, true, here));
		}
		else if(here.below.isEmpty()){
			System.out.println(name);
			here.below.addElement(new TreeNode(name, true, here));
		}
		else{
			for(TreeNode t : here.below){
				if(t.getPath().equals(name)){
					System.out.println("Folder with same name already exists.");
					return;
				}
			}	
			here.below.addElement(new TreeNode(name, true, here));
		}
	}

	public TreeNode open(String name)
	{
		//check for:
		//	      does path exist
		//	      is it a file
		for(TreeNode t : here.below)
		{
			if(t.getPath().equals(name) && t.getIsFile()==true){
				return t;
			}
		}
		return null;
	}

	public void read(String name)
	{
		//call open(); to check if it exists
		TreeNode t = open(name);
		//display contents of file
		if(t!=null){
			try {
				BufferedReader f = new BufferedReader(new FileReader(t.getFile()));
				String line = null;
				while((line = f.readLine()) != null){
					System.out.println(line);
				}
				//call close(); to close the file
				f.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}		
		}
	}

	public void write(String name)
	{
		//call open(); to check if it exists
		TreeNode t = open(name);
		//writes some text to a file
		if(t!=null){
			try {
				BufferedWriter f = new BufferedWriter(new FileWriter(t.getFile()));
				String line = "";
				while(!(line.equals("$done"))){
					line = scan.nextLine();
					if(!(line.equals("$done")))
						f.write(line + "\n");
				}
				//call close(); to close the file
				f.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}


	////////////////////////////////////
	// Folder Commands
	////////////////////////////////////
	public void createFolder(String name)
	{
		if(here == null)
			here = new TreeNode(name, false, null);
		else if(here.below.isEmpty()){
			System.out.println(name);
			here.below.addElement(new TreeNode(name, false, here));
		}
		else{
			for(TreeNode t : here.below){
				if(t.getPath().equals(name)){
					System.out.println("Folder with same name already exists.");
					return;
				}
			}
			here.below.addElement(new TreeNode(name, false, here));	
		}
	}

	public void list()
	{
		//loop through here.below and list all contents
		for(TreeNode t : here.below){
			System.out.print(t.getPath() + " ");
		}
		System.out.print("\n");
	}

	public void cd(String foldername)
	{
		if(foldername.equals("..")){
			here = here.below.get(0).below.get(0);
			return;
		}
		else{
			//traverse here.below 
			for(TreeNode t : here.below){
				//if foldername exists, here now points to that folder
				if(t.getPath().equals(foldername) && t.getIsFile() == false){
					here = t;
					return;
				}
				//give a failure message if foldername is a file
				else if(t.getPath().equals(foldername) && t.getIsFile() == true){
					System.out.println("Can't change directory to a file.");
					return;
				}
			}
		}
		
		//give a failure message if foldername doesn't exist
		System.out.println("Folder does not exist in this directory.");
	}



	////////////////////////////////////
	// Both Commands
	////////////////////////////////////
	public void delete(String name)
	{
		//check if root directory
		//	      if yes : can not delete
		if(name == "root"){
			System.out.println("Can Not Delete The Root Directory");
			return;
		}

		//traverse here
		for(TreeNode t : here.below){
			
			//check if path does exist
			//if folder: check if empty
			if(t.getPath().equals(name) && t.getIsFile() == false){
				//	if empty: delete folder
				if(t.below.isEmpty()){
					here.below.removeElement(t);
					System.out.println("Folder deleted.");
					return;
				}
				//	if not empty: ask if its ok to delete all contents
				else{
					System.out.println("Are you sure you wish to delete this folder and all of its contents? (Y/N)");
					String answer = scan.nextLine();
					//	if no : break
					if(answer.equals("N"))
						return;
					//	if yes : delete folder
					else{
						here.below.removeElement(t);
						System.out.println("Folder deleted.");
						return;
					}
				}				
			}
			
			//if file: ask if its ok to delete file
			else if(t.getPath().equals(name) && t.getIsFile() == true){
				System.out.println("Are you sure you wish to delete this file? (Y/N)");
				String answer = scan.nextLine();
				//	if no : break
				if(answer.equals("N"))
					return;
				//	if yes : delete file
				else{
					here.below.removeElement(t);
					System.out.println("Folder deleted.");
					return;
				}
			}
		}

		//give a failure message if name doesn't exist
		System.out.println("File or Folder does not exist in this directory.");
	}
	
	public void rename(String name)
	{
		//loop through here.below and check if current exists
		for(TreeNode t : here.below){
			//	if does: here.below[i].data = newname
			if(t.getPath().equals(name)){
				System.out.println("Please enter the new name: ");
				String newname = scan.nextLine();
				t.setPath(newname);
				return;
			}
		}
		//	if not: error message
		System.out.println("That file does not exist.");
	}

	public void move(String filename, String filepath)
	{
		cut(filename);
		
		TreeNode movingNode = root;
		String[] Delims = filepath.split("/");
		toploop: for(String s : Delims){
			if(s.equals("root")){
				continue;
			}
			else{
				for(TreeNode t : movingNode.below)
				{
					if(t.getPath().equals(s)){
						System.out.println(s + "worked");
						movingNode = t;
						if(s.equals(Delims[(Delims.length)-1])){
							paste(movingNode);
							return;
						}
						continue toploop;
					}
				}
				
				System.out.println("Invalid path.");
				return;	
			}
		}
	}

	public void copy(String filename)
	{
		for(TreeNode t : here.below){
			if(t.getPath().equals(filename)){
				copied = t;
				return;
			}				
		}
		
		System.out.println("Object does not exist in the current directory.");
	}

	public void cut(String filename)
	{
		copy(filename);
		delete(filename);
	}

	public void paste()
	{
		here.below.addElement(copied);
	}
	
	public void paste(TreeNode a)
	{
		a.below.addElement(copied);
	}
}