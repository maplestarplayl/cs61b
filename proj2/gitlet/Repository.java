package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Map;

import java.time.*;
import java.time.temporal.*;



import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File LOG = join(GITLET_DIR,"log");
    public static final File Objects = join(GITLET_DIR,"objects");
    public static final File Staging_add = join(GITLET_DIR,"staging_add");
    public static final File Commitee = join(GITLET_DIR,"commitee");
    public static final File HEAD = join(GITLET_DIR,"head");
    public static final File master = join(GITLET_DIR,"master");
    public static final File branch = join(GITLET_DIR,"branch");
    /* TODO: fill in the rest of this class. */


    public static void setupPersistence() {
        try {
            if (!GITLET_DIR.exists()) {
                GITLET_DIR.mkdir();
            }
            if (!LOG.exists()) {
                LOG.createNewFile();
            }
            if (!Objects.exists()) {
                Objects.mkdir();
            }
            if (!Staging_add.exists()) {
                Staging_add.mkdir();
            }
            if (!HEAD.exists()){
                HEAD.createNewFile();
            }
            if (!branch.exists()){
                branch.createNewFile();
            }
            if (!Commitee.exists()){
                Commitee.mkdir();
            }
            if (!master.exists()){
                master.createNewFile();
            }

        } catch (IOException excp) {
            System.out.println(excp.getMessage());
        }
    }
    public static boolean checkIfRepository(){
        return GITLET_DIR.exists();
    }
    public static boolean checkIfExistAdd(){
        if (Staging_add.delete()){
            Staging_add.mkdir();
            return true;
        }
        return false;
    }
    public static Commit returnCommitByHead(){
        String index = Utils.readContentsAsString(HEAD);
        File commit = join(Commitee,index);
        Commit com = Utils.readObject(commit,gitlet.Commit.class);
        return com;
    }
    public static Commit returnCommitByIndex(String index) {
        File commit = join(Commitee,index);
        if (!commit.exists()) {
            System.out.println("No commit with that id exists.");
            return null;
        }
        Commit com = Utils.readObject(commit,gitlet.Commit.class);
        return com;
    }
    public static String addHelperGetFileIndex(String name,Commit com){
        Map<String,String> map = com.treeDirectory.returnMap();
        String index = map.get(name);
        return index;
    }
    public static void CommitSaveandUpdatePointer(Commit com){
        String index = Utils.sha1(com);
        File tobesaved = Utils.join(Commitee,index);
        Utils.writeObject(tobesaved,com);
        Utils.writeContents(HEAD,index);
        Utils.writeContents(master,index);
    }
    public static void LogHelperRecordEachCommit(Commit com){
        String identifier = "==="+"\n";
        String last = Utils.readContentsAsString(LOG);
        String firstline = "commit" + " " + sha1(com) + "\n";
        String secondline = "Data:" + com.getTimestamp()+"\n";
        String thirdline = com.getMessage();
        String finalres = last + identifier + firstline + secondline + thirdline + "\n";
        Utils.writeContents(LOG,finalres);
    }
    public static void Fileinitialize(File f){
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

        } catch (IOException excp){
            System.out.println(excp.getMessage());
        }
    }

    public static void init(){
        Commit initial_commit = new Commit("initial commit",null);
        CommitSaveandUpdatePointer(initial_commit);
        LogHelperRecordEachCommit(initial_commit);
    }
    public static void add(String name){
        File tobeadd = join(CWD,name);
        // if file not exists,exit
        if (!tobeadd.exists()){
            Utils.error("File does not exist.");
            System.exit(0);
        }
        //if the file to be added is identical to the current commit,then exit
        Commit commit = returnCommitByHead();
        String oriindex = addHelperGetFileIndex(name,commit);
        String curindex = Utils.sha1(tobeadd);
        if (oriindex == curindex){
            return;
        }
        //copy the file to the staging area
        File copyAdd = join(Staging_add,name);
        try{
            Files.copy(tobeadd.toPath(),copyAdd.toPath());
        }catch (IOException excp){
             System.out.println(excp.getMessage());
        }
    }

    public static void commit(String message){
        // copy the last commit to the new commit
        Commit last = returnCommitByHead();
        Commit now = last;
        now.parent = last;
        now.message = message;
        /* to be solved : the representation of timestamp */

        File[] files = Staging_add.listFiles();
        now.changeTreeDirectory(files);
        LogHelperRecordEachCommit(now);
        CommitSaveandUpdatePointer(now);
        //clear the staging area after commit
        for (File file :files){
            file.delete();
        }
    }
    public static void log(){
        String res = Utils.readContentsAsString(LOG);
        System.out.println(res);
    }

    public static void checkoutHEAD(String fileName){
        Commit com = returnCommitByHead();
        Map map = com.treeDirectory.returnMap();
        if (map.containsKey(fileName)){
            Blob b = Blob.returnBlobByIndex((String)map.get(fileName));
            File tooverride = join(CWD,fileName);
            Fileinitialize(tooverride);
            Utils.writeContents(tooverride,b.getByte());
            return;
        }
        else {
            System.out.println("File does not exist in that commit.");
            return;
        }
    }
    public static void checkooutBeforeCommit(String commitindex,String filename){
        Commit com = returnCommitByIndex(commitindex);
        if (com == null){
            return;
        }
        Map map = com.treeDirectory.returnMap();
        if (map.containsKey(filename)){
            Blob b = Blob.returnBlobByIndex((String)map.get(filename));
            File tooverride = join(CWD,filename);
            Fileinitialize(tooverride);
            Utils.writeContents(tooverride,b.getByte());
            return;
        }
        else {
            System.out.println("File does not exist in that commit.");
            return;
        }

    }




    }
