package com.example.carlos.appcurso.Domain;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Carlos on 28/01/2017.
 */

public class MusicFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}
