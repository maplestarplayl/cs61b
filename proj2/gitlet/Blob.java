package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] bytes;
    private String index;
    Blob(byte[] bytes,String index){
        this.bytes = bytes;
        this.index = index;
        File tobesave = Utils.join(Repository.Objects,index);
        Utils.writeObject(tobesave,this);
    }

    public String getIndex(){
        return this.index;
    }
    public byte[] getByte(){
        return this.bytes;
    }
    public static Blob createBlob(File f){
        byte[] bytes = Utils.readContents(f);
        String strindex = Utils.sha1(bytes);
        return new Blob(bytes,strindex);

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
