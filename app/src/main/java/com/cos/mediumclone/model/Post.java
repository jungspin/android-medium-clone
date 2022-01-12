package com.cos.mediumclone.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private int id;
    private String title;
    private String content;
    private User user;
    private String created;
    private String updated;

    @Override
    public String toString() {
        return
                "id : " + id + '\n' +
                "title : " + title + '\n' +
                "content : " + content + '\n' +
                "user : " + user + '\n' +
                "created : " + created + '\n' +
                "updated : " + updated + '\n';
    }
}
