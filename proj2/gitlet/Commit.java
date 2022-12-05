package gitlet;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Map;
import java.io.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public class TreeDirectory{
        public String name;
        public Map<String,String> map;
        TreeDirectory sub;

        TreeDirectory(File[] files){
            for (File f : files){
                Blob b = Blob.createBlob(f);
                map.put(f.getName(),b.getIndex());
            }
        }
        TreeDirectory(File f){
            if (f.isDirectory())
            {
                name = f.getName();
            }
            else{
                Blob b =Blob.createBlob(f);
                map.put(f.getName(),b.getIndex());
            }
        }
        public Map returnMap(){
            return this.map;
        }
        public void addNewFiles(File[] files){
            for (File f : files){
                Blob b = Blob.createBlob(f);
                map.put(f.getName(),b.getIndex());
            }
        }

    }
    public String message;
    public String timestamp;
    public Commit parent;
    public TreeDirectory treeDirectory;


    /* TODO: fill in the rest of this class. */
    public Commit(String message,Commit parent){
        this.message = message;
        this.parent = parent;
        if (this.parent == null){
            this.timestamp ="00:00:00 UTC, Thursday, 1 January 1970";
        }
    }
    public String getMessage(){ return this.message;}
    public Commit getParent(){return this.parent;}
    public String getTimestamp(){return this.timestamp;};
    //dynamically add new files to the tree directory
    public void changeTreeDirectory(File[] files){
        this.treeDirectory.addNewFiles(files);
    }



















}
