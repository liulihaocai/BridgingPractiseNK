package cn.ricoco.bridgingpractise.Utils;

import cn.ricoco.bridgingpractise.Main;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static void CopyFile(File orifile,File tofile) throws IOException {
        FileInputStream infile=new FileInputStream(orifile);
        FileOutputStream outfile=new FileOutputStream(tofile);
        FileChannel inc=infile.getChannel();
        FileChannel outc=outfile.getChannel();
        inc.transferTo(0,inc.size(),outc);
        inc.close();
        outc.close();
        infile.close();
        outfile.close();
    }
    public static void Copydir(String oridir,String todir) throws IOException {
        File[] flist=new File(oridir).listFiles();
        for(var i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                CopyFile(flist[i],new File(new File(todir).getPath()+"/"+flist[i].getName()));
            }else{
                String faps=flist[i].getPath();
                new File(new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length(),faps.length())+"/").mkdirs();
                Copydir(faps,new File(todir).getPath()+"/"+faps.substring(new File(oridir).getPath().length(),faps.length())+"/");
            }
        }
    }
    public static void deldir(String dir) throws IOException {
        File[] flist=new File(dir).listFiles();
        for(var i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                flist[i].delete();
            }else{
                deldir(flist[i].getPath());
            }
        }
    }
    public static void unescapeB64(String b64,String outdir) throws IOException {
        byte[] b=Base64.getDecoder().decode(b64);
        FileOutputStream out=new FileOutputStream(outdir);
        for (var  i=0;i<b.length;++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        out.write(b);
        out.flush();
        out.close();
    }
    public static String readFile(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            return new String(filecontent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void writeFile(String path,String text) {
        try {
            FileWriter FW = new FileWriter(new File(path));
            FW.write(text);
            FW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void ReadJar(String fileName,String path){
        try {
            String jarName = GetJarDir();
            JarFile jarFile = new JarFile(jarName);
            JarEntry entry = jarFile.getJarEntry(fileName);
            InputStream input = jarFile.getInputStream(entry);
            writeIS(input,new File(path));
            input.close();
            jarFile.close();
        }catch (Exception e){
            System.out.println(new String(String.valueOf(e)));
        }
    }
    private static String GetJarDir(){
        String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        return jarPath;
    }
    private static void writeIS(InputStream input,File file) {
        try {
            java.nio.file.Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
