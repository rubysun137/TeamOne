package com.ruby.teamone;

public class Article {
    private String article_content;
    private String article_id;
    private String article_tag;
    private String article_title;
    private long create_time;
    private String email;

    public Article(String article_content, String article_id, String article_tag, String article_title, long create_time, String email) {
        this.article_content = article_content;
        this.article_id = article_id;
        this.article_tag = article_tag;
        this.article_title = article_title;
        this.create_time = create_time;
        this.email = email;
    }

    public String getArticle_content() {
        return article_content;
    }

    public String getArticle_id() {
        return article_id;
    }

    public String getArticle_tag() {
        return article_tag;
    }

    public String getArticle_title() {
        return article_title;
    }

    public long getCreate_time() {
        return create_time;
    }

    public String getEmail() {
        return email;
    }
}
