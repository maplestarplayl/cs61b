package gitlet;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

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
    public static final File Staging_rem = join(GITLET_DIR,"staging_remove");
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
            if (!Staging_rem.exists()){
                Staging_rem.mkdir();
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
    public static boolean checkIfNotExistAdd(){
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
        if (com.treeDirectory != null) {
            Map<String, String> map = com.treeDirectory.returnMap();
            String index = map.get(name);
            return index;
        }
        return null;
    }
    public static void CommitSaveandUpdatePointer(Commit com){
        String index = com.getHash();
        File tobesaved = Utils.join(Commitee,index);
        Fileinitialize(tobesaved);
        Utils.writeObject(tobesaved,com);
        Utils.writeContents(HEAD,index);
        Utils.writeContents(master,index);
    }
    public static void LogHelperRecordEachCommit(Commit com){
        String identifier = "==="+"\n";
        String last = Utils.readContentsAsString(LOG);
        String firstline = "commit" + " " + com.getHash() + "\n";
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
    public static boolean checkIfFileEqualLastCommit(String name){
        File cur = join(CWD,name);
        Commit com = returnCommitByHead();
        if (addHelperGetFileIndex(name,com) != null){
            String oriindex = addHelperGetFileIndex(name, com);
            byte[] bytee = Utils.readContents(cur);
            String curindex = Utils.sha1(bytee);
            if (oriindex.equals(curindex)) {
                return true;
            }
        }
        return false;
    }

    public static void init(){
        Commit initial_commit = new Commit("initial commit",null);
        initial_commit.calcHash();
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
        if (checkIfFileEqualLastCommit(name)){
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
        now.parent = last.getHash();
        now.message = message;
        /* to be solved : the representation of timestamp */

        File[] files = Staging_add.listFiles();
        File[] files1 = Staging_rem.listFiles();

        now.changeTreeDirectoryAdd(files);
        now.changeTreeDirectoryRemove(files1);
        now.calcHash();
        LogHelperRecordEachCommit(now);
        CommitSaveandUpdatePointer(now);
        //clear the staging area after commit
        for (File file :files){
            file.delete();
        }
        for (File file : files1){
            file.delete();
        }
    }
    public static void log(){
        String res = Utils.readContentsAsString(LOG);
        System.out.println(res);
    }

    public static void checkoutHEAD(String fileName){
        Commit com = returnCommitByHead();
        Map<String,String> map = com.treeDirectory.returnMap();
        if (map.containsKey(fileName)){
            Blob b = Blob.returnBlobByIndex(map.get(fileName));
            File tooverride = join(CWD,fileName);
            Fileinitialize(tooverride);
            Utils.writeContents(tooverride,b.getByte());
        }
        else {
            System.out.println("File does not exist in that commit.");
        }
    }
    public static void checkooutBeforeCommit(String commitindex,String filename){
        Commit com = returnCommitByIndex(commitindex);
        if (com == null){
            System.out.println("No commit with that id exists.");
            return;
        }
        Map<String,String> map = com.treeDirectory.returnMap();
        if (map.containsKey(filename)){
            Blob b = Blob.returnBlobByIndex((String)map.get(filename));
            File tooverride = join(CWD,filename);
            Fileinitialize(tooverride);
            Utils.writeContents(tooverride,b.getByte());
        }
        else {
            System.out.println("File does not exist in that commit.");
        }

    }

    public static void rm(String filename){
        File tobedeleted = join(Staging_add,filename);
        Commit com = returnCommitByHead();
        Map<String,String> map1 = com.treeDirectory.returnMap();
        if (tobedeleted.exists()){
            tobedeleted.delete();
        }
        else if (map1.containsKey(filename)){
            //map1.remove(filename);
            //com.treeDirectory.map = map1;
            //File commitChanged = join(Commitee,com.getHash());
            //Utils.writeObject(commitChanged,com);
            File tobedelete = join(CWD,filename);
            File copyAdd = join(Staging_rem,filename);
            try{
                Files.copy(tobedelete.toPath(),copyAdd.toPath());
            }catch (IOException excp){
                System.out.println(excp.getMessage());
            }
            tobedelete.delete();
        }
        else{
            Utils.exitWithMessage("No reason to remove the file.");
        }

    }

    public static void find(String message){
        File[] files = Commitee.listFiles();
        int fla = 0;
        for (File file: files){
            String name = file.getName();
            Commit com = returnCommitByIndex(name);
            if (com.getMessage().equals(message)){
                System.out.println(name);
                fla += 1;
            }
        }
        if (fla == 0){
            System.out.println("Found no commit with that message.");
        }

    }

    public static void status(){
        List<String> cwdfilenames = new ArrayList<>() ;
        List<String> strnames = Utils.plainFilenamesIn(CWD);
        for (String name :strnames){
            cwdfilenames.add(name);
        }
        // Branches
        System.out.println("=== Branches ===");
        System.out.println("*master");
        System.out.println("");
        //Staged files
        System.out.println("=== Staged Files ===");
        File[] stagedFiles = Staging_add.listFiles();
        for (File file : stagedFiles){
            System.out.println(file.getName());
            cwdfilenames.remove(file.getName());
        }

        //Removed Files
        System.out.println("=== Removed Files ===");
        File[] removedFiles = Staging_rem.listFiles();
        for (File file :removedFiles){
            System.out.println(file.getName());
        }

        // Modifications Not Staged For Commit
        System.out.println("=== Modifications Not Staged For Commit ===");
        if (cwdfilenames != null){
            for (int i = 0,len = cwdfilenames.size();i < len;i++){
                String each = cwdfilenames.get(i);
                if (checkIfFileEqualLastCommit(each)){
                    cwdfilenames.remove(each);
                    len -= 1;
                    i -= 1;
                }
                else if (addHelperGetFileIndex(each,returnCommitByHead()) != null){
                    cwdfilenames.remove(each);
                    len -= 1;
                    i -= 1;
                    System.out.println(each);
                }
            }
        }

        //Untracked Files
        System.out.println("=== Untracked Files ===");
        for (String filename:cwdfilenames){
            System.out.println(filename);
        }
    }






    }
