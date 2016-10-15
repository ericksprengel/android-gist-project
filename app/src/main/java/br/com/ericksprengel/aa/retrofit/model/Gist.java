package br.com.ericksprengel.aa.retrofit.model;


import java.util.Date;
import java.util.Map;

public class Gist {

    public String id;
    public String description;
    public Date created_at;
    public Map<String, GistFile> files;
    public User owner;
}
