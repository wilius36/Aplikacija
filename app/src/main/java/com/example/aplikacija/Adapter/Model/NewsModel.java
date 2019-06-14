package com.example.aplikacija.Adapter.Model;

public class NewsModel {

    String sectionNameNews;
    String titleNews;
    String webPublicationDateNews;
    String webUrl;

    public NewsModel() {
    }

    public NewsModel(String sectionNameNews, String titleNews, String webPublicationDateNews, String webUrl) {
        this.sectionNameNews = sectionNameNews;
        this.titleNews = titleNews;
        this.webPublicationDateNews = webPublicationDateNews;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSectionNameNews() {
        return sectionNameNews;
    }

    public void setSectionNameNews(String sectionNameNews) {
        this.sectionNameNews = sectionNameNews;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }

    public String getWebPublicationDateNews() {
        return webPublicationDateNews;
    }

    public void setWebPublicationDateNews(String webPublicationDateNews) {
        this.webPublicationDateNews = webPublicationDateNews;
    }
}
