package gitlet;

// TODO: any imports you need here

import java.nio.file.Files;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.nio.file.Path;

import static gitlet.Utils.join;


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
    public class TreeDirectory implements Serializable{
        public String name;
        public Map<String,String> map = new HashMap<>();
        TreeDirectory sub;
        /** Not dealing with the subDirectories */
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
        public Map<String,String> returnMap(){
            return this.map;
        }
        public void addNewFiles(File[] files){
            for (File f : files){
                Blob b = Blob.createBlob(f);
                map.put(f.getName(),b.getIndex());
            }
        }
        public void RemoveFile(File[] files){
            for (File f : files){
                String index =map.get(f.getName());
                File blogsave = join(Repository.Objects,index);
                blogsave.delete();
                map.remove(f.getName());
            }
        }

    }
    public String message;
    public String timestamp;
    public String parent;
    public TreeDirectory treeDirectory;
    public String hash;
    //Calculate the hash value according to the current commit
    public void calcHash() {
        File f = join(Repository.GITLET_DIR,"try");
        Repository.Fileinitialize(f);
        Utils.writeObject(f,this);
        try {
            Path path = Path.of(f.getPath());
            byte[] bytee = Files.readAllBytes(path);
            hash = Utils.sha1(bytee);
            f.delete();
        } catch (IOException e) {
            System.err.println("Exception of reading files:" + e.getMessage());
        }

    }

    /* TODO: fill in the rest of this class. */
    public Commit(String message,String parentindex){
        this.message = message;
        this.parent = parentindex;
        this.treeDirectory = null;
        if (this.parent == null){
            this.timestamp ="00:00:00 UTC, Thursday, 1 January 1970";
        }
    }
    public String getMessage(){ return this.message;}
    public String getTimestamp(){return this.timestamp;}
    //dynamically add new files to the tree directory
    public void changeTreeDirectoryAdd(File[] files){
        if (this.treeDirectory == null){
            this.treeDirectory = new TreeDirectory(files);
        }
        else{
            this.treeDirectory.addNewFiles(files);
        }
    }

    public void changeTreeDirectoryRemove(File[] files){
        if (this.treeDirectory == null){
            Utils.exitWithMessage("This is a null treedirectory");
        }
        else{
            this.treeDirectory.RemoveFile(files);
        }
    }
    //Remember call the method "calcHash" before this method
    public String getHash(){
        return hash;
    }


















}
