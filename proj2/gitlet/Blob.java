package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] bytee;
    private String index;
    Blob(byte[] byteee,String indexx){
        this.bytee = byteee;
        this.index = indexx;
        File tobesave = Utils.join(Repository.Objects,index);
        Utils.writeObject(tobesave,this);
    }

    public String getIndex(){
        return this.index;
    }
    public byte[] getByte(){
        return this.bytee;
    }
    public static Blob createBlob(File f){
        byte[] bytee = Utils.readContents(f);
        String strindex = Utils.sha1(bytee);
        return new Blob(bytee,strindex);

    }
    public static Blob returnBlobByIndex(String index){
        File toberead = Utils.join(Repository.Objects,index);
        if (!toberead.exists()){
            return null;
        }
        Blob b = Utils.readObject(toberead,Blob.class);
        return b;
    }


}
