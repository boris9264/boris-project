package com.boris.chapter1;

import java.io.File;

public class QueryHiddenFile {
    public static void main(String[] args) {
        File[] hiddenFiles = QueryHiddenFile.queryHiddenFiles("D:\\forlder");
        for (File hiddenFile : hiddenFiles) {
            System.out.println(hiddenFile.getName());
        }
    }

/*    public static File[] queryHiddenFiles(String path) {
        File[] hiddenFiles = new File(path).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });

        return hiddenFiles;
    }*/

    //方法引用 :: 语法，把isHidden函数传递给listFiles方法
    public static File[] queryHiddenFiles(String path) {
        File[] hiddenFiles = new File(path).listFiles(File::isHidden);
        return hiddenFiles;
    }
}
